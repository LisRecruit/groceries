package com.example.groceries.store;

import com.example.groceries.auth.user.CustomUserDetails;
import com.example.groceries.family.CurrentFamilyService;
import com.example.groceries.family.Family;
import com.example.groceries.store.dtos.StoreCreateRequest;
import com.example.groceries.store.dtos.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/store")
public class StoreController {
    private final StoreService storeService;
    private final CurrentFamilyService currentFamilyService;

    @PostMapping
    public ResponseEntity<StoreResponse> create (@RequestBody StoreCreateRequest request,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails){
        Family family = currentFamilyService.getFamilyFromUser(userDetails);
        StoreResponse response = storeService.create(request, family);
        return ResponseEntity.ok(response);
    }
}
