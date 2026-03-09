package com.example.groceries.family;

import com.example.groceries.auth.user.User;
import com.example.groceries.auth.user.UserService;
import com.example.groceries.family.dtos.responses.FamilyResponse;
import com.example.groceries.family.dtos.responses.StringResponse;
import com.example.groceries.groceries.grocery_list.GroceryList;
import com.example.groceries.groceries.grocery_list.GroceryListService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserService userService;
    private final FamilyMapper familyMapper;
    private final GroceryListService groceryListService;

    public FamilyResponse getFamilyByCode(int code){
        Family family = familyRepository.findByCode(code)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found"));
        return familyMapper.familyToFamilyResponse(family);
    }

    @Transactional
    public StringResponse addUserToFamily(int familyCode, Long userId) {
        Family family = familyRepository.findByCode(familyCode)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Family not found"));
        User user = userService.getUserById(userId);
        if (user.getFamily() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already belongs to a family");
        }
        family.addUser(user);
        return new StringResponse("User added to family");
    }

    @Transactional
    public FamilyResponse createFamily (Long userId, String familyName) {
        User user = userService.getUserById(userId);
        if (user.getFamily() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already belongs to a family");
        }
        Family family = Family.builder()
                .name(familyName)
                .code(generateFamilyCode())
                .owner(user)
                .build();
        family.addUser(user);
        GroceryList groceryList = GroceryList.builder().build();
        family.setGroceryList(groceryList);

        Family savedFamily = familyRepository.save(family);

        return familyMapper.familyToFamilyResponse(savedFamily);
    }

    @Transactional
    public StringResponse removeFamilyMember (Long ownerId, Long userToRemoveId, Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(()-> new EntityNotFoundException("Family not found"));
        if (!ownerId.equals(family.getOwner().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only owner can remove users");
        }

        User userToRemove = userService.getUserById(userToRemoveId);
        if (!family.getUsers().contains(userToRemove)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not belongs to this family");
        }

        if (ownerId.equals(userToRemoveId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner cannot be removed");
        }

        family.removeUser(userToRemove);
        return new StringResponse("User removed from family");
    }

    private int generateFamilyCode (){
        int code;
        do {
            code = ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
        } while (familyRepository.existsByCode(code));
        return code;
    }


}
