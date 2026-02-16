package com.example.groceries.family;

import com.example.groceries.auth.user.User;
import com.example.groceries.auth.user.UserService;
import com.example.groceries.family.dtos.requests.FamilyByCodeRequest;
import com.example.groceries.family.dtos.responses.FamilyResponse;
import com.example.groceries.family.dtos.responses.StringResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyRepository familyRepository;
    private final UserService userService;
    private final FamilyMapper familyMapper;

    public FamilyResponse getFamilyByCode(FamilyByCodeRequest request){
        Family family = familyRepository.findByCode(request.code())
                .orElseThrow(()->new EntityNotFoundException("Family not found"));
        return familyMapper.familyToFamilyResponse(family);
    }

    @Transactional
    public StringResponse addUserToFamily(int familyCode, Long userId) {
        Family family = familyRepository.findByCode(familyCode)
                .orElseThrow(()->new EntityNotFoundException("Family not found"));
        User user = userService.getUserById(userId);
        if (user.getFamily() != null) {
            throw new IllegalStateException("User already belongs to a family");
        }
        family.addUser(user);
        return new StringResponse("User add to family");
    }

    @Transactional
    public FamilyResponse createFamily (Long userId, String familyName) {
        User user = userService.getUserById(userId);
        if (user.getFamily() != null) {
            throw new IllegalStateException("User already belongs to a family");
        }
        Family family = Family.builder()
                .name(familyName)
                .code(generateFamilyCode())
                .owner(user)
                .build();
        family.addUser(user);
        Family newFamily = familyRepository.save(family);
        return familyMapper.familyToFamilyResponse(newFamily);
    }

    @Transactional
    public StringResponse removeFamilyMember (Long ownerId, Long userToRemoveId, Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(()-> new EntityNotFoundException("Family not found"));
        User userToRemove = userService.getUserById(userToRemoveId);
        if (!ownerId.equals(family.getOwner().getId())) {
            throw new IllegalStateException("Only owner can remove users");
        }

        if (!family.getUsers().contains(userToRemove)) {
            throw new IllegalStateException("User does not belong to this family");
        }

        if (ownerId.equals(userToRemoveId)) {
            throw new IllegalStateException("Owner cannot be removed");
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
