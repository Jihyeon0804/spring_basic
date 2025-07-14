package com.beyond.basic.b2_board.controller;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDTO;
import com.beyond.basic.b2_board.dto.AuthorDetailDTO;
import com.beyond.basic.b2_board.dto.AuthorListDTO;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDTO;
import com.beyond.basic.b2_board.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController             // @Controller + @ResponseBody : 데이터만 주고 받음, 화면X
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;

    // 회원 가입
    @PostMapping("/create")
    public String save(@RequestBody AuthorCreateDTO authorCreateDTO) {
        try {
            this.authorService.save(authorCreateDTO);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "OK";
    }
    
    // 회원 목록 조회 : /author/list
    @GetMapping("/list")
    public List<AuthorListDTO> findAll() {
        return authorService.findAll();
    }
    
    
    // 회원 상세 조회 by id : /author/detail/1
    // 서버에서 별도의 try-catch를 하지 않으면, 에러 발생 시 500 error + 스프링의 포맷으로 error return
    @GetMapping("/detail/{id}")
//    public Author findById(@PathVariable Long id) {
    public AuthorDetailDTO findById(@PathVariable Long id) {
        try {
            return authorService.findById(id);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
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
