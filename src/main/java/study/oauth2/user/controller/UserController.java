package study.oauth2.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.user.domain.dto.FollowRequestDto;
import study.oauth2.user.domain.dto.ProfileDto;
import study.oauth2.user.service.FollowService;
import study.oauth2.user.service.ProfileService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final ProfileService profileService;
    private final FollowService followService;

    @GetMapping("/users")
    public String getUserInfo(@AuthenticationPrincipal UserDetails UserDetails) {
        return UserDetails.getUsername();
    }

    @PostMapping("/users/profile")
    public ResponseEntity<?> setUserProfile(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody ProfileDto profileDto
    ) {
        profileService.saveUserProfile(userDetails.getUsername(), profileDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/follow/add")
    public ResponseEntity<?> followingUser(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody FollowRequestDto followingRequestDto
    ) {
        followService.followingUser(userDetails.getUsername(), followingRequestDto.getToUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/follow/delete")
    public ResponseEntity<?> unFollowingUser(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody FollowRequestDto followingRequestDto
    ) {
        followService.deleteFollower(userDetails.getUsername(), followingRequestDto.getToUser());
        return ResponseEntity.ok().build();
    }
}
