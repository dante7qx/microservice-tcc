package org.dante.et.springboot.storage.dao;

import org.dante.et.springboot.storage.po.StoragePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StorageDAO extends JpaRepository<StoragePO, Long> {
	
	@Modifying
	@Query("update StoragePO set lockCount = lockCount + ?2 where commodityCode = ?1 and totalCount > 0")
	public void decreaseStock(String commodityCode, int count);
	
	@Modifying
	@Query("update StoragePO set lockCount = lockCount - ?2, totalCount = totalCount - ?2 where commodityCode = ?1 and totalCount > 0")
	public void confirm4Stock(String commodityCode, int count);
	
	@Modifying
	@Query("update StoragePO set lockCount = lockCount - ?2 where commodityCode = ?1 and totalCount > 0")
	public void cancel4Stock(String commodityCode, int count);
	
	public StoragePO findByCommodityCode(String commodityCode);
	
}
