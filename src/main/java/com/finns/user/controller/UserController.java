package com.finns.user.controller;

import com.finns.post.dto.PostResponseDTO;
import com.finns.user.dto.User;
import com.finns.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(value = "AmountByDateController", tags = "날짜별 총 소비 정보")
@PropertySource({"classpath:/application.properties"})
@CrossOrigin(origins = "http://localhost:5173") // 클라이언트의 도메인을 허용
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{no}")
    public ResponseEntity<User> postInfo(@PathVariable("no") Long userNo) {
        User user = userService.getUser(userNo);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/users/{no}/mbti")
    public ResponseEntity<?> analysisMbti(@PathVariable("no") Long userNo) {
        userService.setMbtiByCategory(userNo);
        return ResponseEntity.ok().build();
    }
}
