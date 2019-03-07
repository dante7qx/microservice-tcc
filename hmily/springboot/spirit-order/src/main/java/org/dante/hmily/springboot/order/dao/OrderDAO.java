package org.dante.hmily.springboot.order.dao;

import org.dante.hmily.springboot.order.po.OrderPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrderDAO extends JpaRepository<OrderPO, Long> {
	
	@Modifying
	@Query("update OrderPO set status = ?2 where id = ?1")
	public void updateOrderStatus(Long id, String status);
	
}
