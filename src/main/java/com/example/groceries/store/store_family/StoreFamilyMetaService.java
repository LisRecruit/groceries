package com.example.groceries.store.store_family;

import com.example.groceries.family.Family;
import com.example.groceries.store.Store;
import com.example.groceries.store.StoreRepository;
import com.example.groceries.store.dtos.StoreResponse;
import com.example.groceries.store.store_family.dtos.SetCustomNameRequest;
import com.example.groceries.store.store_family.dtos.SetDescriptionRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class StoreFamilyMetaService {
    private final StoreFamilyMetaRepository storeFamilyMetaRepository;
    private final StoreRepository storeRepository;
    @Transactional
    public StoreResponse setCustomName (SetCustomNameRequest request, Family family) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Store not found"));
        StoreFamilyMeta storeFamilyMeta = storeFamilyMetaRepository.findByStoreAndFamily(store, family)
                .orElseGet(()-> StoreFamilyMeta.builder()
                                .store(store)
                                .family(family)
                                .build()
                );
        storeFamilyMeta.setCustomName(request.customName());
        StoreFamilyMeta savedStoreFamilyMeta = storeFamilyMetaRepository.save(storeFamilyMeta);
        return new StoreResponse(store.getId(),
                savedStoreFamilyMeta.getCustomName(),
                store.getLocation().getLatitude(),
                store.getLocation().getLongitude(),
                savedStoreFamilyMeta.getDescription());
    }

    @Transactional
    public StoreResponse setDescription (SetDescriptionRequest request, Family family) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Store not found"));
        StoreFamilyMeta storeFamilyMeta = storeFamilyMetaRepository.findByStoreAndFamily(store, family)
                .orElseGet(()-> StoreFamilyMeta.builder()
                        .store(store)
                        .family(family)
                        .build()
                );
        storeFamilyMeta.setDescription(request.description());
        StoreFamilyMeta savedStoreFamilyMeta = storeFamilyMetaRepository.save(storeFamilyMeta);
        String storeNameForResponse;
        if (savedStoreFamilyMeta.getCustomName()==null || savedStoreFamilyMeta.getCustomName().isBlank()){
            storeNameForResponse = store.getName();
        } else {
            storeNameForResponse = savedStoreFamilyMeta.getCustomName();
        }
        return new StoreResponse(store.getId(),
                storeNameForResponse,
                store.getLocation().getLatitude(),
                store.getLocation().getLongitude(),
                savedStoreFamilyMeta.getDescription());

    }
}
