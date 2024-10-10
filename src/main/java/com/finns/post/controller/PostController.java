package com.finns.post.controller;

import com.finns.post.dto.ChangeRenewStatusDTO;
import com.finns.post.dto.PostRequestByCategoryDTO;
import com.finns.post.dto.PostRequestByDateDTO;
import com.finns.post.dto.PostResponseDTO;
import com.finns.post.service.PostService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalTime.now;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(value = "PostController", tags = "게시글 정보")
@PropertySource({"classpath:/application.properties"})
@CrossOrigin(origins = "http://localhost:5173") // 클라이언트의 도메인을 허용
public class PostController {

    private final PostService postService;

    @GetMapping("/posts/{no}")
    public ResponseEntity<PostResponseDTO> postInfo(@PathVariable("no") Long no) {
        PostResponseDTO post = postService.getPost(no);
        return ResponseEntity.ok().body(post);
    }

    @PostMapping("/posts/byDate")
    public ResponseEntity<List<PostResponseDTO>> postsInfoByUserAndDateAndIsPublic(@RequestBody PostRequestByDateDTO postRequestByDateDTO) {
        List<PostResponseDTO> posts = postService.getPostsByUserAndDateAndIsPublic(postRequestByDateDTO);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/posts/byCategory")
    public ResponseEntity<List<PostResponseDTO>> postsInfoByUserAndCategoryAndIsPublic(@RequestBody PostRequestByCategoryDTO postRequestByCategoryDTO) {
        List<PostResponseDTO> posts = postService.getPostsByUserAndCategoryAndIsPublic(postRequestByCategoryDTO);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/users/{no}/renew")
    public ResponseEntity<?> renewPosts(@PathVariable("no") Long userNo) {
        LocalDateTime now = LocalDateTime.now();

        ChangeRenewStatusDTO changeRenewStatusDTO = new ChangeRenewStatusDTO(userNo, now);
        postService.updateRenewStatusAndAmount(changeRenewStatusDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{no}/posts/count")
    public ResponseEntity<Long> countPosts(@PathVariable("no") Long userNo) {
        Long count = postService.getCountByUser(userNo);
        return ResponseEntity.ok(count);
    }

    @PutMapping("posts/{no}/togglePublicStatus")
    public ResponseEntity<?> togglePublicStatus(@PathVariable("no") Long no) {
        postService.reversePublicStatus(no);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/images/distinct")
    public ResponseEntity<List<Long>> getDistinctPostNos() {
        List<Long> distinctPostNos = postService.getDistinctPostNos();
        return ResponseEntity.ok(distinctPostNos);
    }
}
