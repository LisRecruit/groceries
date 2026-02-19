package com.example.groceries.groceries.grocery_list;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroceryListRepository extends JpaRepository<GroceryList, Long> {
    Optional<GroceryList> findByFamilyId (Long familyId);

    @Query("""
        SELECT DISTINCT gl
        FROM GroceryList gl
        JOIN gl.items i
        JOIN i.stores s
        WHERE gl.family.id = :familyId
          AND s.id = :storeId
    """)
    Optional<GroceryList> findByFamilyIdAndByItemWithStoreId( @Param("familyId") Long familyId,
                                                              @Param("storeId") Long storeId);
}
