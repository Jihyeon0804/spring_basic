package com.beyond.basic.b1_hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// Component 어노테이션을 통해 별도의 객체를 생성할 필요가 없는 싱글톤 객체 생성
// Controller 어노테이션을 통해 쉽게 사용자의 http request를 분석하고 http response를 생성
@Controller
// 클래스 차원 url(공통된 url 주소) 매핑시에는 RequestMapping을 사용
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
    public String param2(@RequestParam(value = "name") String inputName,
                         @RequestParam(value = "email") String inputEmail) {
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
    public String path(@PathVariable Long inputId) {
        // 별도의 형 변환 없이도 매개변수에 타입 지정 시 자동 형변환 시켜줌
//        long id = Long.parseLong(inputId);
        System.out.println(inputId);
        return "OK";
    }
    
    // case6. parameter가 많아질 경우, 데이터 바인딩을 통해 input값 처리
    // 데이터 바인딩 : parameter를 사용하여 객체로 생성
    // ?name=hong&email=hong@naver.com
    @GetMapping("/param3")
    @ResponseBody
//    public String param3(Hello hello) {
    // @ModelAttribute를 써도 되고 안써도 되는데, 이 키워드를 써서 명시적으로 param의 형식의 데이터를 받겠다라는 것을 표현
    public String param3(@ModelAttribute Hello hello) {
        // 수 많은 파라미터가 넘어오면 어차피 객체로 만들 것이기 때문에
        // 애초에 받아올 때부터 객체로 받아오기
        return "OK";
    }

    // case7. 서버에서 화면을 return, 사용자로부터 넘어오는 input 값을 활용하여 동적인 화면 생성
    // 서버에서 화면(+데이터)을 렌더링 해주는 SSR 방식 (CSR 방식은 서버에서 데이터만)
    // MVC(Model, View, Controller) 패턴이라고도 함
    @GetMapping("/model-param")
    public String modelParm(@RequestParam(value = "id") Long inputId, Model model) {
        // Model 객체는 데이터를 화면에 전달해주는 역할
        // name 이라는 key에 hong 이라는 value를 key : value 형태로 화면에 전달
        if (inputId == 1) {
            model.addAttribute("name", "hong");
            model.addAttribute("email", "hong@naver.com");
        } else if (inputId == 2) {
            model.addAttribute("name", "hong2");
            model.addAttribute("email", "hong2@naver.com");
        }
        return "helloworld2";
    }


    // post 요청의 case들 : url 인코딩 방식과 multipart-formdata, json
    // case1. text만 있는 form-data  형식
    // 형식 : body에 name=xxx&email=xxx
    @GetMapping("/form-view")
    public String formView() {
        return "form-view";
    }

    @PostMapping("/form-view")
    @ResponseBody
    // get 요청에 url에 파라미터 방식과 동일한 데이터 형식이므로 RequestParam 또는 데이터 바인딩 방식 가능
    public String formViewPost(@ModelAttribute Hello hello) {
        System.out.println(hello);
        return "OK";
    }


    // case2-1. text와 file이 있는 form-data 형식 (순수 html로 제출)
    @GetMapping("/form-file-view")
    public String formFileView() {
        return "form-file-view";
    }

    @PostMapping("/form-file-view")
    @ResponseBody
    public String formFileViewPost(@ModelAttribute Hello hello,
                                   @RequestParam(value = "photo") MultipartFile photo) {
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "OK";
    }

    // case2-2. text와 file이 있는 form-data 형식 (js로 제출)


    // case3. text와 멀티 file이 있는 form-data 형식


    // case4. json 데이터 전송

    
    // case5. 중첩된 json 데이터 처리


    // case6. json과 file을 같이 처리할 때

}
