package com.beyond.basic.b2_board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonErrorDTO {

    private int status_code;
    private String status_message;

}
