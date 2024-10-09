package com.finns.card.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private long card_no;
    private String card_name;
    private String corp_name;
    private String img_url;
    private long ex_l_mth;
    private String ex_in_for;
    private String detail_link;
    private String card_type_no;
    private String type;
    private String benefit;
    private String benefit_detail;
}
