package com.example.groceries.family;

import com.example.groceries.auth.security.JwtUtil;
import com.example.groceries.auth.user.CustomUserDetails;
import com.example.groceries.family.dtos.requests.CreateFamilyRequest;

import com.example.groceries.family.dtos.responses.FamilyResponse;
import com.example.groceries.family.dtos.responses.StringResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/family")
public class FamilyController {
    private final FamilyService familyService;

    @GetMapping("/{code}")
    public ResponseEntity<FamilyResponse> getFamily (@PathVariable int code) {
        FamilyResponse response = familyService.getFamilyByCode(code);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/add/{familyCode}")
    public ResponseEntity<StringResponse> addUserToFamily (@PathVariable int familyCode,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        StringResponse response = familyService.addUserToFamily(familyCode, userDetails.getUserId());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<FamilyResponse> create (@RequestBody CreateFamilyRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        FamilyResponse response = familyService.createFamily(userDetails.getUserId(), request.name());
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{familyId}/member/{userId}")
    public ResponseEntity<StringResponse> removeUserFromFamily(@PathVariable Long userId,
                                                               @PathVariable Long familyId,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        StringResponse response = familyService.removeFamilyMember(userDetails.getUserId(), userId, familyId);
        return ResponseEntity.ok(response);

    }
}
