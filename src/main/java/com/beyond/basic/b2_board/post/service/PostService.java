package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDTO;
import com.beyond.basic.b2_board.post.dto.PostDetailDTO;
import com.beyond.basic.b2_board.post.dto.PostListDTO;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
//@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDTO postCreateDTO) {
        // authorId가 실제 있는 지 없는 지 검증 필요
        Author author = authorRepository.findById(postCreateDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        postRepository.save(postCreateDTO.toEntity(author));

    }

    public PostDetailDTO findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 Id 입니다."));

        // 엔티티간 관계성 설정하지 않았을 경우
//        Author author = authorRepository.findById(post.getAuthorId())
//                .orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
//
//        return PostDetailDTO.fromEntity(post, author);
        
        // 엔티티 간 관계성 설정을 통해 Author 객체를 쉽게 조회하는 경우
        return PostDetailDTO.fromEntity(post);

    }

    public List<PostListDTO> findAll() {
        return postRepository.findAll().stream().map(a -> PostListDTO.fromEntity(a)).collect(Collectors.toList());
    }
}
