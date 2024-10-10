package com.finns.greatOrStupid.controller;


import com.finns.greatOrStupid.service.GreatOrStupidService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(value = "GreatOrStupidController", tags = "좋아요 싫어요")
@PropertySource({"classpath:/application.properties"})
@CrossOrigin(origins = "http://localhost:5173") // 클라이언트의 도메인을 허용
@RestController
@RequestMapping("/greatorstupid")
@RequiredArgsConstructor
public class GreatOrStupidController {

    private final GreatOrStupidService greatOrStupidService;

    @PostMapping("/{userNo}/{postNo}")
    public ResponseEntity<Void> toggleGreat(@PathVariable Long userNo, @PathVariable Long postNo) {
        greatOrStupidService.toggleGreat(userNo, postNo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userNo}/{postNo}/isGreat")
    public ResponseEntity<Boolean> isGreat(@PathVariable Long userNo, @PathVariable Long postNo) {
        Boolean result = greatOrStupidService.isGreat(userNo, postNo);
        return ResponseEntity.ok(result);
    }
}