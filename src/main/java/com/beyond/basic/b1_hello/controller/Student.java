package com.beyond.basic.b1_hello.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String name;
    private String email;
    private List<Score> scoreList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Score {
        private String subject;
        private int point;
    }
}
