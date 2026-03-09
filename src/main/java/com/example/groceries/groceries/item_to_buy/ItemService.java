package com.example.groceries.groceries.item_to_buy;

import com.example.groceries.auth.user.User;
import com.example.groceries.auth.user.UserRepository;
import com.example.groceries.family.Family;
import com.example.groceries.family.FamilyRepository;
import com.example.groceries.groceries.grocery_list.GroceryList;
import com.example.groceries.groceries.grocery_list.GroceryListRepository;
import com.example.groceries.groceries.item_to_buy.dtos.*;
import com.example.groceries.store.Store;
import com.example.groceries.store.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final StoreRepository storeRepository;
    private final GroceryListRepository groceryListRepository;
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;

    public ItemResponse getItem (ItemRequest request) {
        Item item = itemRepository.findById(request.id())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "item not found"));

        return itemMapper.toResponse(item);
    }


    private ItemResponse createItem(ItemCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Family family = user.getFamily();
        if (family==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not have family");
        }
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found"));
        Item item = Item.builder()
                .name(request.name())
                .quantity(request.quantity())
                .note(request.note())
                .build();
        item.addStore(store);
        family.getGroceryList().addItem(item);

        familyRepository.save(family);

        return itemMapper.toResponse(item);
    }

    @Transactional
    public List<ItemResponse> createSetOfItems (CreateSetOfItemsRequest request, Long userId) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item list is empty");
        }
        return request.items().stream()
                .map(item -> createItem(item, userId))
                .toList();
    }

    private void deleteItem (ItemRequest request, Family family) {
        Item itemToDelete = itemRepository.findById(request.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        if (!itemToDelete.getGroceryList().getFamily().equals(family)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You don't have access to this grocery list"
            );
        }
        GroceryList list = itemToDelete.getGroceryList();
        list.removeItem(itemToDelete);
    }
    @Transactional
    public void deleteSetOfItems(DeleteSetOfItemsRequest request, Family family) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item list is empty");
        }

        request.items().forEach(item ->
                deleteItem(item, family)
        );
    }
}
