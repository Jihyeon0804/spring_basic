package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.author.service.AuthorService;
import com.beyond.basic.b2_board.common.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController             // @Controller + @ResponseBody : 데이터만 주고 받음, 화면X
@RequiredArgsConstructor    // 생성자 주입 (반드시 final 붙여주어야 함)
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원 가입
    @PostMapping("/create")
//    public ResponseEntity<String> save(@RequestBody AuthorCreateDTO authorCreateDTO) {          // 입력값과 도메인 필드가 다르면 새로운 객체 생성(사용자 입력과 응답 DTO 따로 설계; DTO 에는 setter 설정 포함(@Data), 일반 도메인에는 getter만(@DataX))
    // DTO에 있는 validation 어노테이션 (@NotEmpty, @Size 등)과 controller의 @Valid는 한 쌍
    public ResponseEntity<?> save(@Valid @RequestBody AuthorCreateDTO authorCreateDTO) {
//    public String save(@RequestBody AuthorCreateDTO authorCreateDTO) {
        /*
        try {       // error 발생했는데  try-catch 해주지 않으면 500 Internal Server Error
            this.authorService.save(authorCreateDTO);
//            return new ResponseEntity<>("OK", HttpStatus.CREATED);
            return new ResponseEntity<>(new CommonDTO("OK", HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // 생성자 매개변수로 응답을 줄 body 부분의 객체와 header 부의 상태코드 입력
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(new CommonDTO("BAD REQUEST", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()), HttpStatus.BAD_REQUEST);
//            return e.getMessage();          // return String -> 200 OK
        }
        */
        // controllerAdvice가 없었으면 위와 같이 개별적인 예외 처리가 필요하나, 이제는 전역적인 예외 처리가 가능
        this.authorService.save(authorCreateDTO);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    // 로그인 : /author/doLogin
    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody AuthorLoginDTO authorLoginDTO) {
        Author author = authorService.doLogin(authorLoginDTO);

        // 토큰 생성 및 return
        String token = jwtTokenProvider.createAtToken(author);
        return new ResponseEntity<>(new CommonDTO(token, HttpStatus.OK.value(), "token is created"), HttpStatus.OK);
    }

    
    // 회원 목록 조회 : /author/list => admin 만 가능하도록 설정
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")           // JwtTokenFilter 에서 authentication 객체를 만들 때 ROLE_ 붙인 걸 떼서 검사
    // 권한이 n개인 경우 아래처럼 사용 (토큰 만들 때도 authorities.add를 n개 해주어야 함)
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public List<AuthorListDTO> findAll() {
        return authorService.findAll();
    }
    

    // 회원 상세 조회 by id : /author/detail/1 => admin 만 가능하도록 설정
    // 서버에서 별도의 try-catch를 하지 않으면, 에러 발생 시 500 error + 스프링의 포맷으로 error return
    @GetMapping("/detail/{id}")
    // ADMIN 권한이 있는 지를 authentication 객체에서 쉽게 확인
    // 권한이 없을 경우 filterChain 에서 에러 발생
    @PreAuthorize("hasRole('ADMIN')")
//    public Author findById(@PathVariable Long id) {
//    public AuthorDetailDTO findById(@PathVariable Long id) {
    public ResponseEntity<?> findById(@PathVariable Long id) {
        /*
        try {
//            return new ResponseEntity<>(authorService.findById(id), HttpStatus.OK);
            return new ResponseEntity<>(new CommonDTO(authorService.findById(id), HttpStatus.OK.value(), "author is found"), HttpStatus.OK);
//            return authorService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new CommonErrorDTO(HttpStatus.NOT_FOUND.value(), "author is not found"), HttpStatus.NOT_FOUND);
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
         */
        return new ResponseEntity<>(new CommonDTO(authorService.findById(id), HttpStatus.OK.value(), "author is found"), HttpStatus.OK);
    }

    @GetMapping("/myInfo")
    public ResponseEntity<?> myInfo() {
        return new ResponseEntity<>(new CommonDTO(authorService.myInfo(), HttpStatus.OK.value(), "myInfo is found"), HttpStatus.OK);
    }
    
    
    // 회원 정보(비밀번호) 수정 : email, password -> json
    // patch(부분 수정), put(대체)
    @PatchMapping("/updatepw")
    public void update(@RequestBody AuthorUpdatePwDTO authorUpdatePwDTO) {
        authorService.updatePassword(authorUpdatePwDTO);
    }

    // 회원 탈퇴(삭제) /author/delete/1
    // delete
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
