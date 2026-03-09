package com.example.groceries.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {


    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
            "FROM Store s " +
            "WHERE s.name = :name " +
            "AND ABS(s.location.latitude - :latitude) <= 0.0002 " +
            "AND ABS(s.location.longitude - :longitude) <= 0.0002")
    boolean existsByNameAndApproxLocation(
            @Param("name") String name,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude
    );
    List<Store> findAllByVerifiedFalse();
}
