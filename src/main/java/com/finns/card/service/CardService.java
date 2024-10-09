package com.finns.card.service;

import com.finns.card.dto.Card;
import com.finns.card.dto.CardDetailDTO;
import com.finns.card.dto.CardProductDTO;
import com.finns.card.mapper.CardMapper;
import com.finns.card.pagination.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardMapper cardMapper;

    public CardService(CardMapper cardMapper) {
        this.cardMapper = cardMapper;
    }

    public PageResponse<CardProductDTO> getCards(int page, int size) {
        int offset = (page - 1) * size; // 페이지 수에 따라 오프셋 계산
        List<Card> cards = cardMapper.selectAllCards(offset, size);

        List<CardProductDTO> cardProductDTOs = cards.stream()
                .map(card -> new CardProductDTO(card.getCard_no(), card.getCard_name(), card.getCorp_name(),
                        card.getImg_url(), card.getBenefit(), card.getDetail_link(), card.getEx_l_mth(),
                        card.getEx_in_for()))
                .collect(Collectors.toList());

        long totalElements = cardMapper.countCards(); // 총 카드 수를 가져옴
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PageResponse<>(cardProductDTOs, totalPages, totalElements, size, page);
    }

    public CardDetailDTO getCardById(long card_no) {
        Card card = cardMapper.selectCardById(card_no);
        return new CardDetailDTO(card.getCard_no(), card.getCard_name(), card.getCorp_name(), card.getImg_url(),
                card.getEx_l_mth(), card.getEx_in_for(), card.getDetail_link(),
                card.getBenefit_detail());
    }
}
