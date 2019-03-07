package org.dante.hmily.springboot.account.dao;

import java.math.BigDecimal;

import org.dante.hmily.springboot.account.po.AccountPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccountDAO extends JpaRepository<AccountPO, Long> {
	
	@Modifying
	@Query("update AccountPO set frozen = frozen + ?2, amount = amount - ?2 where userId = ?1 and amount > 0")
	public void decreaseAmount(String userId, BigDecimal amount);
	
	@Modifying
	@Query("update AccountPO set frozen = frozen - ?2 where userId = ?1 and frozen >= 0")
	public void confirm4Amount(String userId, BigDecimal amount);
	
	@Modifying
	@Query("update AccountPO set frozen = frozen - ?2, amount = amount + ?2 where userId = ?1 and frozen >= 0")
	public void cancel4Amount(String userId, BigDecimal amount);
	
	public AccountPO findByUserId(String userId);
}
