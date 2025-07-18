package com.beyond.basic.b2_board.post.controller;

import com.beyond.basic.b2_board.author.dto.CommonDTO;
import com.beyond.basic.b2_board.post.dto.PostCreateDTO;
import com.beyond.basic.b2_board.post.dto.PostDetailDTO;
import com.beyond.basic.b2_board.post.dto.PostListDTO;
import com.beyond.basic.b2_board.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> findAll() {
        List<PostListDTO> postListDTo = postService.findAll();
        return new ResponseEntity<>(new CommonDTO(postListDTo, HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        PostDetailDTO postDetailDTO = postService.findById(id);
        return new ResponseEntity<>(new CommonDTO(postDetailDTO, HttpStatus.OK.value()
                , "post is found"), HttpStatus.OK);
    }
}
