package com.beyond.basic.b2_board.author.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonDTO {

    private Object result;
    private int status_code;
    private String status_message;

}
