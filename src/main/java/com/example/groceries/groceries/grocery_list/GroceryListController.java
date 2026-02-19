package com.example.groceries.groceries.grocery_list;

import com.example.groceries.auth.user.CustomUserDetails;
import com.example.groceries.auth.user.User;
import com.example.groceries.auth.user.UserService;
import com.example.groceries.family.dtos.responses.StringResponse;
import com.example.groceries.groceries.grocery_list.dtos.GroceryListResponse;
import com.example.groceries.groceries.item_to_buy.dtos.ItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/groceries")
public class GroceryListController {
    private final GroceryListService groceryListService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<GroceryListResponse> getGroceriesForFamilyByCurrentUser (
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(groceryListService.getGroceryListByFamily(getFamilyIdFromUser(userDetails)));
    }
    @GetMapping("/{storeId}")
    public ResponseEntity<GroceryListResponse> getGroceriesByStoreForCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long storeId){
        return ResponseEntity.ok(groceryListService
                .getGroceryListByStoreAndFamily(storeId, getFamilyIdFromUser(userDetails)));
    }

    @PatchMapping
    public ResponseEntity<StringResponse> addItemsToGroceryList (@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody List<ItemRequest> requests){
        groceryListService.addItemsToGroceryList(requests, getFamilyIdFromUser(userDetails));
        return ResponseEntity.ok(new StringResponse("Items added"));
    }

    @DeleteMapping("/items")
    public ResponseEntity<StringResponse> removeItemsFromGroceryList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @RequestBody List<ItemRequest> requests){
        groceryListService.removeItemsFromGroceryList(requests, getFamilyIdFromUser(userDetails));
        return ResponseEntity.ok(new StringResponse("Items removed"));
    }


    private Long getFamilyIdFromUser (CustomUserDetails userDetails) {
        User user = userService.getUserById(userDetails.getUserId());
        if (user.getFamily()==null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User does not belong to any family"
            );
        }
        return user.getFamily().getId();
    }
}
