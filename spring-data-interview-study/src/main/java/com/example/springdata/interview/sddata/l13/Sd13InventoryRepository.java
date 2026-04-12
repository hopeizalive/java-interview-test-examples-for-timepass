package com.example.springdata.interview.sddata.l13;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Sd13InventoryRepository extends JpaRepository<Sd13Inventory, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Sd13Inventory i set i.quantity = i.quantity + :delta where i.productCode = :code")
    int adjustStock(@Param("code") String productCode, @Param("delta") int delta);
}
