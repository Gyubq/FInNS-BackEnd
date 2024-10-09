package com.finns.card.controller;

import com.finns.card.dto.CardDetailDTO;
import com.finns.card.dto.CardProductDTO;
import com.finns.card.pagination.PageResponse;
import com.finns.card.service.CardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/api/cards")
    public PageResponse<CardProductDTO> getCards(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return cardService.getCards(page, size);
    }

    @GetMapping("/api/cards/{id}")
    public CardDetailDTO getCard(@PathVariable long id) {
        return cardService.getCardById(id);
    }
}
