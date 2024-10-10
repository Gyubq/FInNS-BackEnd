package com.finns.greatOrStupid.mapper;

import com.finns.greatOrStupid.dto.GreatOrStupid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GreatOrStupidMapper {
    // 데이터 삽입 및 업데이트 메서드
    void insertOrUpdate(GreatOrStupid greatOrStupidDTO);

    // 사용자 번호와 게시물 번호에 따른 isGreat 값을 조회하는 메서드
    Boolean findByUserNoAndPostNo(@Param("userNo") Long userNo, @Param("postNo") Long postNo);
}
