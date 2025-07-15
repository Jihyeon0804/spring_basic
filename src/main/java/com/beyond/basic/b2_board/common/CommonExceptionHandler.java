package com.beyond.basic.b2_board.common;

import com.beyond.basic.b2_board.dto.CommonErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

// @Controller가 붙어있는 클래스의 모든 예외를 모니터링하여 예외를 인터셉트
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    // 상황에 따라 다른 에러가 파라미터로 주입됨
    public ResponseEntity<?> illegalException(IllegalArgumentException e) {
        return new ResponseEntity<>(new CommonErrorDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> noSuchException(IllegalArgumentException e) {
        return new ResponseEntity<>(new CommonErrorDTO(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
