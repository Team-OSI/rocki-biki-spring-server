package study.oauth2.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.user.domain.dto.FollowCountResponseDto;
import study.oauth2.user.domain.dto.FollowRequestDto;
import study.oauth2.user.domain.dto.ProfileResponseDto;
import study.oauth2.user.domain.dto.UserInfoResponseDto;
import study.oauth2.user.service.FollowService;
import study.oauth2.user.service.ProfileService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final ProfileService profileService;
    private final FollowService followService;

    @PostMapping("/profile/set")
    public ResponseEntity<?> setUserProfile(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam @Valid @NotNull(message = "Nickname cannot be null") String nickname,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        ProfileResponseDto profileResponseDto = profileService.updateUserProfile(userDetails.getUsername(), nickname, image);
        return ResponseEntity.ok(profileResponseDto);
    }

    @GetMapping("/profile/get")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponseDto userProfile = profileService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(userProfile);
    }

    @PostMapping("/profile/addSound")
    public ResponseEntity<?> addUserSound(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestPart(value = "sound", required = false) MultipartFile sound,
        @RequestPart(value = "old_url", required = false) String oldUrl
    ) {
        profileService.addUserSound(userDetails.getUsername(), sound, oldUrl);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/follow/add")
    public ResponseEntity<?> followingUser(
        @AuthenticationPrincipal UserDetails userDetails,
        @Valid @RequestBody FollowRequestDto followingRequestDto
    ) {
        followService.followingUser(userDetails.getUsername(), followingRequestDto.getToUser());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/follow/delete")
    public ResponseEntity<?> unFollowingUser(
        @AuthenticationPrincipal UserDetails userDetails,
        @Valid @RequestBody FollowRequestDto followingRequestDto
    ) {
        followService.deleteFollower(userDetails.getUsername(), followingRequestDto.getToUser());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/follow/count")
    public ResponseEntity<?> followCount (@AuthenticationPrincipal UserDetails userDetails) {
        FollowCountResponseDto followCountResponseDto = followService.followCount(userDetails.getUsername());
        return ResponseEntity.ok(followCountResponseDto);
    }

    @GetMapping("/follow/getFollowList")
    public ResponseEntity<?> getFollowList (@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(followService.followList(userDetails.getUsername()));
    }

    @GetMapping("/userInfo")
    public ResponseEntity<?> getUserInfo (@RequestParam("userEmail") String userEmail) {
        UserInfoResponseDto userInfoResponseDto = profileService.getUserInfo(userEmail);
        return ResponseEntity.ok(userInfoResponseDto);
    }

}
