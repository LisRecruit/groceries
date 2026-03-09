package com.example.groceries.store;

import com.example.groceries.family.Family;
import com.example.groceries.store.dtos.StoreCreateRequest;
import com.example.groceries.store.dtos.StoreResponse;
import com.example.groceries.store.store_family.StoreFamilyMeta;
import com.example.groceries.store.store_family.StoreFamilyMetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreFamilyMetaRepository storeFamilyMetaRepository;


    @Transactional
    public StoreResponse create (StoreCreateRequest request, Family family) {
        if (storeRepository.existsByNameAndApproxLocation(request.name(), request.latitude(), request.longitude())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Store already exist");
        }
        Store storeToCreate = Store.builder()
                .name(request.name())
                .location(StoreLocation.builder()
                        .latitude(request.latitude())
                        .longitude(request.longitude())
                        .build())
                .verified(false)
                .createdByFamily(family)
                .build();
        Store newStore = storeRepository.save(storeToCreate);
        if (request.description() != null && !request.description().isBlank()){
            StoreFamilyMeta storeFamilyMeta = StoreFamilyMeta.builder()
                    .family(family)
                    .store(newStore)
                    .description(request.description())
                    .build();
            storeFamilyMetaRepository.save(storeFamilyMeta);
        }
        return StoreResponse.builder()
                .id(newStore.getId())
                .name(newStore.getName())
                .latitude(newStore.getLocation().getLatitude())
                .longitude(newStore.getLocation().getLongitude())
                .description(request.description() != null ? request.description() : "")
                .build();
    }
    //only for admin
    @Transactional
    public void approveStore (Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Store not found"));
        store.setVerified(true);
        storeRepository.save(store);
    }
    //only for admin
    @Transactional
    public void rejectStore(Long rejectedStoreId, Long correctStoreId){
        Store rejectedStore = storeRepository.findById(rejectedStoreId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Store not found"));
        Store correctStore = storeRepository.findById(correctStoreId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Store not found"));
        List<StoreFamilyMeta> storeFamilyMetaList = storeFamilyMetaRepository.findAllByStore(rejectedStore);

        String rejectedName = rejectedStore.getName();

        if (storeFamilyMetaList.isEmpty()) {
            StoreFamilyMeta storeFamilyMeta = StoreFamilyMeta.builder()
                    .family(rejectedStore.getCreatedByFamily())
                    .store(correctStore)
                    .customName(rejectedName)
                    .build();
            storeFamilyMetaRepository.save(storeFamilyMeta);
        }

        storeFamilyMetaList.forEach(meta -> {
            meta.setStore(correctStore);
            if (meta.getFamily().getId().equals(rejectedStore.getCreatedByFamily().getId())) {
                meta.setCustomName(rejectedName);
            }
        });

        storeFamilyMetaRepository.saveAll(storeFamilyMetaList);
        storeRepository.delete(rejectedStore);
    }

    //only for admins
    public List<Store> getAllUnverifiedStores() {
        return storeRepository.findAllByVerifiedFalse();
    }
}
