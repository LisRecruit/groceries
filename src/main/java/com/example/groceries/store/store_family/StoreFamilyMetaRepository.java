package com.example.groceries.store.store_family;

import com.example.groceries.family.Family;
import com.example.groceries.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreFamilyMetaRepository extends JpaRepository<StoreFamilyMeta, Long> {
    List<StoreFamilyMeta> findAllByStore(Store store);
    Optional<StoreFamilyMeta> findByStoreAndFamily(Store store, Family family);
}
