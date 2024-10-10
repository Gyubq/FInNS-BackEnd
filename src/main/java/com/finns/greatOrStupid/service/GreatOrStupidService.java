package com.finns.greatOrStupid.service;

import com.finns.greatOrStupid.dto.GreatOrStupid;
import com.finns.greatOrStupid.mapper.GreatOrStupidMapper;
import com.finns.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreatOrStupidService {

    private final GreatOrStupidMapper greatOrStupidMapper;
    private final PostMapper postMapper;

    public void toggleGreat(Long userNo, Long postNo) {
        // 현재 isGreat 값을 조회
        Boolean currentIsGreat = greatOrStupidMapper.findByUserNoAndPostNo(userNo, postNo);

        // 현재 isGreat가 null인 경우, 기본값을 false로 설정
        if (currentIsGreat == null) {
            currentIsGreat = false;
        }

        // isGreat 값을 토글
        Boolean newIsGreat = !currentIsGreat;

        // GreatOrStupid 테이블에 삽입 또는 업데이트
        GreatOrStupid greatOrStupidDTO = new GreatOrStupid(userNo, postNo, newIsGreat);
        greatOrStupidMapper.insertOrUpdate(greatOrStupidDTO);

        // isGreat 값에 따라 각 카운트를 증가
        if (newIsGreat) {
            postMapper.incrementGreatCount(postNo); // true일 경우 great_count 증가
        } else {
            postMapper.incrementStupidCount(postNo); // false일 경우 stupid_count 증가
        }
    }

    public Boolean isGreat(Long userNo, Long postNo) {
        return greatOrStupidMapper.findByUserNoAndPostNo(userNo, postNo);
    }
}
