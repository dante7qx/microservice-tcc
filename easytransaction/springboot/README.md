## 集成 EasyTransaction

### SpringCloud

#### 1. 依赖

```xml
<!-- 业务模块子系统 -->
<dependency>
    <groupId>com.yiqiniu.easytrans</groupId>
    <artifactId>easytrans-starter</artifactId>
    <version>1.2.0</version>
    <!-- 没有使用队列，因此将其排除，否则将会尝试初始化 -->
    <exclusions>
        <exclusion>
            <groupId>com.yiqiniu.easytrans</groupId>
            <artifactId>easytrans-queue-kafka-starter</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<!-- TCC 共享 Req/Resp 子项目 -->
<dependency>
    <groupId>com.yiqiniu.easytrans</groupId>
    <artifactId>easytrans-core</artifactId>
    <version>1.2.0</version>
</dependency>
```

#### 2. 开发

- 启动类

  添加注解 `@EnableEasyTransaction`

- 软事务参数

  ```java
  /**
  	整个事务涉及类必须一致
  **/
  @Data
  @BusinessIdentifer(appId = "account-service", busCode = "pay", rpcTimeOut = 2000)
  public class AccountPayRequest implements TccMethodRequest<AccountPayResponse> {
  	private static final long serialVersionUID = 1L;	
  	private String userId;
  	private double amount;
  }
  ```

- 事务参与者

  ```java
  /**
    TCC 事务处理类
  **/
  @Component
  public class AccountTccService implements TccMethod<AccountPayRequest, AccountPayResponse> {
  	
  	@Autowired
  	private IAccountService accountService;
  
  	@Override
  	public AccountPayResponseBO doTry(AccountPayRequestBO param) {
  		accountService.decreaseAmount(param.getUserId(), param.getAmount());
  		AccountPayResponseBO response = new AccountPayResponseBO();
  		response.setFrozen(param.getAmount());
  		return response;
  	}
  
  	@Override
  	public void doConfirm(AccountPayRequestBO param) {
  		accountService.confirmDecreaseAmount(param.getUserId(), param.getAmount());
  	}
  
  	@Override
  	public void doCancel(AccountPayRequestBO param) {
  		accountService.cancelDecreaseAmount(param.getUserId(), param.getAmount());
  	}
  
  	@Override
  	public int getIdempotentType() {
  		return IDENPOTENT_TYPE_FRAMEWORK;	// 0: 由 ET 框架负责幂等性 1: 由业务方负责幂等性
  	}
  
  }
  ```

  ```yaml
  ## 必须先启动 Zookeeper
  easytrans:
    master:
      zk:
        zooKeeperUrl: localhost:2181
    stringcodec:
      zk:
        zooKeeperUrl: ${easytrans.master.zk.zooKeeperUrl}
    idgen:
      trxId:
        zkSnow:
          zooKeeperUrl: ${easytrans.master.zk.zooKeeperUrl}
    log:
      database:
        logCleanEnabled: true
        logReservedDays: 14
        logCleanTime: 00:30:00
        druid:
          driverClassName: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://localhost:3306/et_account_translog?characterEncoding=UTF-8&useSSL=false
          username: root
          password: iamdante
  ```

- 事务发起方

  ```java
  @Slf4j
  @Service
  @Transactional(readOnly = true)
  public class OrderServiceImpl implements IOrderService {
  	
  	@Autowired
  	private OrderDAO orderDAO;
  	@Resource
  	private EasyTransFacade transaction;
  
  	@Override
  	@Transactional
  	public OrderDTO createOrder(String userId, String commodityCode, int totalCount, double amount) {
  		OrderPO order = orderDAO.save(buildOrder(userId, commodityCode, totalCount, amount));
  		OrderDTO orderDTO = new OrderDTO();
  		BeanUtils.copyProperties(order, orderDTO);
  		log.info("创建订单 ========> {}", orderDTO);
  		
  		// 声明全局事务ID，其由appId,业务代码，业务代码内ID构成
  transaction.startEasyTrans(OrderConstant.APPID.concat("|").concat(BUSINESSCODE), orderDTO.getId());
  		
  		// 扣减库存
  		decreaseStock(commodityCode, totalCount);
  		
  		// 帐户扣款
  		decreaseAccount(userId, amount);
  		
  		return orderDTO;
  	}
      
      ......
      
      /**
  	 * 帐户扣款
  	 * 
  	 * @param userId
  	 * @param amount
  	 * @return
  	 */
  	private Future<AccountPayResponseBO> decreaseAccount(String userId, double amount) {
  		AccountPayRequestBO accountPayRequest = new AccountPayRequestBO();
  		accountPayRequest.setUserId(userId);
  		accountPayRequest.setAmount(amount);
  		Future<AccountPayResponseBO> accountPayFuture = transaction.execute(accountPayRequest);
  		return accountPayFuture;
  	}
  }
  ```

  ```yaml
  easytrans:
    master:
      zk:
        zooKeeperUrl: localhost:2181
    stringcodec:
      zk:
        zooKeeperUrl: ${easytrans.master.zk.zooKeeperUrl}
    idgen:
      trxId:
        zkSnow:
          zooKeeperUrl: ${easytrans.master.zk.zooKeeperUrl}
    log:
      database:
        logCleanEnabled: true
        logReservedDays: 14
        logCleanTime: 00:28:00
        druid:
          driverClassName: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://localhost:3306/et_order_translog?characterEncoding=UTF-8&useSSL=false
          username: root
          password: iamdante
  ```

  