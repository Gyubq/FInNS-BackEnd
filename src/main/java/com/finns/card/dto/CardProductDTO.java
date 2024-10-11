package com.finns.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardProductDTO {
    private long card_no;
    private String card_name;
    private String corp_name;
    private String img_url;
    private String benefit;
    private String detail_link;
    private long ex_l_mth;
    private String ex_in_for;
}
