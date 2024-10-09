package com.finns.card.mapper;

import com.finns.card.dto.Card;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CardMapper {
    // 카드목록 조회 (페이지네이션 포함)
    List<Card> selectAllCards(@Param("offset") int offset, @Param("limit") int limit);


    long countCards(); // 전체 카드 반환

    Card selectCardById(long card_no); // 특정 카드 조회
}
