package com.beyond.basic.b1_hello.controller;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

//@Getter                       // 클래스 내의 모든 변수를 대상으로 getter 생성
@Data                           // getter, setter, toString 메서드까지 모두 생성해주는 어노테이션
@AllArgsConstructor             // 모든 매개변수가 있는 생성자
@NoArgsConstructor              // 기본 생성자
// 기본 생성자 + getter의 조합은 parsing이 이루어지므로 보통을 필수적인 요소
public class Hello {
    private String name;
    private String email;
//    private MultipartFile photo;

//    public Hello(String name, String email) {
//        this.name = name;
//        this.email = email;
//    }

//    public String getName() {
//        return name;
//    }

//    public String getEmail() {
//        return email;
//    }

//    @Override
//    public String toString() {
//        return "Hello{" +
//                "name='" + name + '\'' +
//                ", email='" + email + '\'' +
//                '}';
//    }
}
