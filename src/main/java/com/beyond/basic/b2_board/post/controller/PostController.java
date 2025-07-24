package com.beyond.basic.b2_board.post.controller;

import com.beyond.basic.b2_board.author.dto.CommonDTO;
import com.beyond.basic.b2_board.post.dto.PostCreateDTO;
import com.beyond.basic.b2_board.post.dto.PostDetailDTO;
import com.beyond.basic.b2_board.post.dto.PostListDTO;
import com.beyond.basic.b2_board.post.dto.PostSearchDTO;
import com.beyond.basic.b2_board.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        postService.save(postCreateDTO);
        return new ResponseEntity<>(new CommonDTO("OK", HttpStatus.CREATED.value()
                , "post is created"), HttpStatus.CREATED);
    }

    // 게시글 목록 조회
    @GetMapping("/list")
//    public ResponseEntity<?> findAll() {
    public ResponseEntity<?> findAll() {
        List<PostListDTO> postListDTO = postService.findAll();

        return new ResponseEntity<>(new CommonDTO(postListDTO, HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }

    // 게시글 목록 조회 - 페이징 처리
    // 페이징 처리를 위한 데이터 요청 형식 : post/list?page=0&size=20&sort=title,desc
    @GetMapping("/listPaging")
    public ResponseEntity<?> findAll(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                     Pageable pageable, PostSearchDTO postSearchDTO) {
        System.out.println(postSearchDTO);
        Page<PostListDTO> postListDTO = postService.findAll(pageable, postSearchDTO);
//        return new ResponseEntity<>(new CommonDTO(postListDTO, HttpStatus.OK.value(), "OK"), HttpStatus.OK);
        return new ResponseEntity<>(
                CommonDTO.builder()
                .result(postListDTO)
                .status_code(HttpStatus.OK.value())
                .status_message("OK")
                .build()
                , HttpStatus.OK);
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        PostDetailDTO postDetailDTO = postService.findById(id);
        return new ResponseEntity<>(new CommonDTO(postDetailDTO, HttpStatus.OK.value()
                , "post is found"), HttpStatus.OK);
    }
}
