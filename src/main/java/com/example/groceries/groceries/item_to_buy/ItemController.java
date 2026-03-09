package com.example.groceries.groceries.item_to_buy;

import com.example.groceries.auth.user.CustomUserDetails;
import com.example.groceries.auth.user.UserService;
import com.example.groceries.family.CurrentFamilyService;
import com.example.groceries.family.Family;
import com.example.groceries.groceries.item_to_buy.dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final CurrentFamilyService currentFamilyService;

    @PostMapping
    public ResponseEntity<List<ItemResponse>> create (@RequestBody CreateSetOfItemsRequest requests,
                                                @AuthenticationPrincipal CustomUserDetails userDetails){
        Long userId = userDetails.getUserId();
        List<ItemResponse> response = itemService.createSetOfItems(requests, userId);
        return ResponseEntity.ok(response);

    }
    @GetMapping
    public ResponseEntity<ItemResponse> getItem (@RequestBody ItemRequest request){
        return ResponseEntity.ok(itemService.getItem(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteItems(@RequestBody DeleteSetOfItemsRequest request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails){
        Family family = currentFamilyService.getFamilyFromUser(userDetails);
        itemService.deleteSetOfItems(request, family);
        return ResponseEntity.noContent().build();
    }

}
