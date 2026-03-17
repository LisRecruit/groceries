package com.example.groceries.admin;

import com.example.groceries.store.StoreService;
import com.example.groceries.store.dtos.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin")
public class AdminController {

    private final StoreService storeService;

    @GetMapping("/unverified")
    public ResponseEntity<List<StoreResponse>> getUnverified () {
        return ResponseEntity.ok(storeService.getAllUnverifiedStores());
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<?> approve ( @PathVariable Long id) {
        storeService.approveStore(id);
        return ResponseEntity.ok("Store approved");
    }

    @PatchMapping("/reject/{rejectedId}/{correctId}")
    public ResponseEntity<?> reject (@PathVariable Long rejectedId, @PathVariable Long correctId) {
        storeService.rejectStore(rejectedId, correctId);
        return ResponseEntity.ok("Store rejected");
    }
}
