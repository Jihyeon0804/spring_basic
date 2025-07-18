package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.CommonDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/response/entity")
public class ResponseEntityController {

    // case1. @ResponseStatus
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/annotation1")
    public String annotation1() {
        return "OK";
    }


    // case2. 메서드 체이닝(Method Chaining) 방식
    @GetMapping("/chaining1")
    public ResponseEntity<?> chaining1() {
        Author author = new Author("test", "test@naver.com", "123456");
        return ResponseEntity.status(HttpStatus.OK).body(author);
    }


    // case3. ResponseEntity 객체를 직접 생성하는 방식 (가장 많이 사용)
    @GetMapping("/custom1")
    public ResponseEntity<?> custom1() {
        Author author = new Author("test", "test@naver.com", "123456");
        return new ResponseEntity<>(author, HttpStatus.CREATED);
    }

    @GetMapping("/custom2")
    public ResponseEntity<?> custom2() {
        Author author = new Author("test", "test@naver.com", "123456");
        return new ResponseEntity<>(new CommonDTO(author, HttpStatus.CREATED.value(), "author is created successfully"), HttpStatus.CREATED);
//        return new ResponseEntity<>(new CommonDTO(author, HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase()), HttpStatus.CREATED);
    }

}
