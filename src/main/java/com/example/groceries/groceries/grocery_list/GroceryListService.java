package com.example.groceries.groceries.grocery_list;

import com.example.groceries.family.Family;
import com.example.groceries.family.FamilyRepository;
import com.example.groceries.groceries.grocery_list.dtos.GroceryListResponse;
import com.example.groceries.groceries.item_to_buy.Item;
import com.example.groceries.groceries.item_to_buy.ItemRepository;
import com.example.groceries.groceries.item_to_buy.dtos.ItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroceryListService {
    private final GroceryListRepository groceryListRepository;
    private final ItemRepository itemRepository;
    private final GroceryListMapper groceryListMapper;
    private final FamilyRepository familyRepository;

    public GroceryListResponse getGroceryListByFamily (Long familyId) {
        if (familyId==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found");
        }
        GroceryList groceryList = groceryListRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grocery list not found"));
        return groceryListMapper.toResponse(groceryList);
    }

    public GroceryListResponse getGroceryListByStoreAndFamily (Long storeId, Long familyId) {
        List<Item> items =
                itemRepository.findItemsByFamilyAndStore(familyId, storeId);
        return groceryListMapper.itemListToResponse(items);
    }

    @Transactional
    public void addItemsToGroceryList (List<ItemRequest> requests, Long familyId) {
        if (familyId==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found");
        }
        GroceryList groceryList = groceryListRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grocery list not found"));

        List<Long> ids = requests.stream().map(ItemRequest::id).toList();
        List<Item> items = itemRepository.findAllById(ids);

        if (items.size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more items not found");
        }

        items.forEach(groceryList::addItem);
        groceryListRepository.save(groceryList);
    }

    @Transactional
    public void removeItemsFromGroceryList (List<ItemRequest> requests, Long familyId) {
        if (familyId==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found");
        }
        GroceryList groceryList = groceryListRepository.findByFamilyId(familyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grocery list not found"));

        List<Long> ids = requests.stream().map(ItemRequest::id).toList();
        List<Item> items = itemRepository.findAllById(ids);

        if (items.size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more items not found");
        }

        items.forEach(groceryList::removeItem);
        groceryListRepository.save(groceryList);
    }

}
