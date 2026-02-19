package com.example.groceries.groceries.item_to_buy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("""
        SELECT DISTINCT i
        FROM Item i
        JOIN i.groceryList gl
        JOIN i.whereToBuy s
        WHERE gl.family.id = :familyId
          AND s.id = :storeId
    """)
    List<Item> findItemsByFamilyAndStore(
            @Param("familyId") Long familyId,
            @Param("storeId") Long storeId
    );
}
