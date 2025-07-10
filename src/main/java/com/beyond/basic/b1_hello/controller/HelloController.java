package com.beyond.basic.b1_hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// Component 어노테이션을 통해 별도의 객체를 생성할 필요가 없는, 싱글톤 객체 생성
// Controller 어노테이션을 통해 쉽게 사용자의 http request를 분석하고 http response를 생성
@Controller
// 클래스 차원 url 매핑시에는 RequestMapping을 사용
@RequestMapping("/hello")
public class HelloController {

    // get 요청의 case들
    // case1. 서버가 사용자에게 단순 String 데이터 return - @ResponseBody가 있을 때
    @GetMapping("")                       // 아래 메서드에 대한 서버의 엔드 포인트를 설정
    // @ResponseBody가 없고 return 타입이 String인 경우 서버는 templates 폴더 밑에 helloworld.html 파일을 찾아서 return
//    @ResponseBody
    public String helloWorld() {
//        return "hello world";
        return "helloworld";
    }

    // case2. 서버가 사용자에게 String(json 형식)의 데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Hello helloJson() throws JsonProcessingException {
        Hello h1 = new Hello("hong", "hong@naver.com");
        // 직접 json으로 직렬화할 필요 없이 return 타입에 객체가 있으면 자동으로 직렬화
        // 객체 return 시 json으로 반환
//        ObjectMapper objectMapper = new ObjectMapper();
//        String result = objectMapper.writeValueAsString(h1);
        return h1;
    }

    // case3. parameter 방식을 통해 사용자로부터 값을 수신
    // parameter의 형식 : /member?name=hongildong
    @GetMapping("/param")
    @ResponseBody
    public Hello param(@RequestParam(value = "name") String inputName) {    // name 파라미터에 inputName 값 할당
        Hello hello = new Hello(inputName, "user1@naver.com");
//        System.out.println(inputName);
        return hello;
    }
    
    // case4. parameter가 2개 이상인 경우
    // /hello/param2?name=hong&email=hong@naver.com
    @GetMapping("/param2")
    @ResponseBody
    public String param2(@RequestParam(value = "name") String inputName, @RequestParam(value = "email") String inputEmail) {
        System.out.println(inputName);
        System.out.println(inputEmail);
        return "ok";
    }

    // case5. PathVariable 방식을 통해 사용자로부터 값을 수신
    // PathVariable 형식 : /member/1
    // PathVariable 방식은 url을 통해 자원의 구조를 명확하게 표현할 때 사용(좀 더 restful함)
    // restful : api로서 구조가 합의한대로 잘 짜여진 형식
    @GetMapping("/path/{inputId}")
    @ResponseBody
    public String path(@PathVariable long inputId) {
        // 별도의 형 변환 없이도 매개변수에 타입 지정 시 자동 형변환 시켜줌
//        long id = Long.parseLong(inputId);
        System.out.println(inputId);
        return "OK";
    }
}
