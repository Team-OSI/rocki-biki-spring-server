package study.oauth2.user.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.oauth2.user.domain.dto.FollowCountResponseDto;
import study.oauth2.user.domain.dto.FollowListResponseDto;
import study.oauth2.user.domain.dto.FollowSimpleDto;
import study.oauth2.user.domain.entity.Follow;
import study.oauth2.user.domain.entity.User;
import study.oauth2.user.repository.FollowRepository;
import study.oauth2.user.repository.UserRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;

	@Transactional
	public void followingUser(String fromUser, String toUser) {
		User user = userRepository.findByEmail(fromUser)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		User followingUser = userRepository.findByEmail(toUser)
			.orElseThrow(() -> new UsernameNotFoundException("Following user not found"));
		followRepository.save(Follow.create(followingUser.getId(), user.getId()));
	}

	@Transactional
	public void deleteFollower(String fromUser, String toUser) {
		User user = userRepository.findByEmail(fromUser)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		User followingUser = userRepository.findByEmail(toUser)
			.orElseThrow(() -> new UsernameNotFoundException("Following user not found"));
		followRepository.deleteByToUserAndFromUser(followingUser.getId(), user.getId());
		followRepository.deleteByToUserAndFromUser(user.getId(), followingUser.getId());
	}

	public FollowCountResponseDto followCount(String email) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		Long followingCount = followRepository.countByFromUser(user.getId());
		Long followerCount = followRepository.countByToUser(user.getId());
		return FollowCountResponseDto.create(followingCount, followerCount);
	}

	public FollowListResponseDto followList(String email) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		log.info("user: {}", user.toString());

		List<FollowSimpleDto> allFollowing = followRepository.findAllFollowing(user.getId());
		List<FollowSimpleDto> allFollower = followRepository.findAllFollower(user.getId());
		for (FollowSimpleDto followSimpleDto : allFollowing) {
			log.info("following: {}", followSimpleDto.toString());
		}
		for (FollowSimpleDto followSimpleDto : allFollower) {
			log.info("follower: {}", followSimpleDto.toString());
		}
		return FollowListResponseDto.of(
			allFollowing,
			allFollower
		);
	}

}
