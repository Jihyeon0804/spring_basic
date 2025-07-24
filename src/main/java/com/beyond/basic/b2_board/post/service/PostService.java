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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // token 안에 들어있는 정보의 email 이기 때문에 조작 불가, 이걸로 모든 인증 진행
        String email = authentication.getName();            // claims 의 subject : email

        Author author = authorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));

        // authorId가 실제 있는 지 없는 지 검증 필요
//        Author author = authorRepository.findById(postCreateDTO.getAuthorId())
//                .orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));

        LocalDateTime appointmentTime = null;

        if (postCreateDTO.getAppointment().equals("Y")) {
            // 예외 처리 - 예약 설정 해놓고 시간을 입력하지 않았거나 비워져 있는 경우
            if (postCreateDTO.getAppointmentTime() == null || postCreateDTO.getAppointmentTime().isEmpty()) {
                throw new IllegalArgumentException("시간 정보가 비워져 있습니다.");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(postCreateDTO.getAppointmentTime(), dateTimeFormatter);
        }

        postRepository.save(postCreateDTO.toEntity(author, appointmentTime));

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
//        List<Post> postList = postRepository.findAll();                 // 일반 전체 조회 -> N + 1 문제 발생
//        List<Post> postList = postRepository.findAllJoin();             // 일반 inner join -> N + 1 문제 발생
//        List<Post> postList = postRepository.findAllFetchJoin();        // inner join fetch -> 1개의 쿼리만 발생
        // postList 조회할 때 참조 관계에 있는 author 까지 조회하게 되기 때문에 N + 1 문제 발생 (N : author 쿼리, 1 : post 쿼리)
        // JPA는 기본 방향성이 fetch lazy 이므로 참조하는 시점에 쿼리를 내보내게 되어 JOIN문을 만들어 주지 않고, N + 1 문제 발생
        // 이후에는 페이징 처리할 것이라 findAll() 안함


        // 페이징 처리 findAll()
        List<Post> postList = postRepository.findAll();
        return postList.stream().map(a -> PostListDTO.fromEntity(a)).collect(Collectors.toList());
    }

    public Page<PostListDTO> findAll(Pageable pageable) {
        // 페이징 처리 findAll()
//        Page<Post> postList = postRepository.findAll(pageable);
//        Page<Post> postList = postRepository.findAllByDelYn(pageable, "N");
        Page<Post> postList = postRepository.findAllByDelYnAndAppointment(pageable, "N", "N");
        return postList.map(a -> PostListDTO.fromEntity(a));
    }
}
