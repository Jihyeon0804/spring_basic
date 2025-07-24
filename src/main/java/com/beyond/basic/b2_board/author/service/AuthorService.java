package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Transactional      // 스프링에서 메서드 단위로 트랜잭션 처리(Commit)하고, 만약 예외(unchecked) 발생 시 자동 롤백 처리 지원
@Service            // transaction 처리가 없는 경우에는 @Component로 대체 가능
@RequiredArgsConstructor
public class AuthorService {

    // 의존성 주입(DI)
    // 방법1) @Autowired 사용 -> 필드 주입
//    @Autowired
//    private AuthorRepositoryInterface authorRepository;

    // 방법2)  생성자 주입 방식 (가장 많이 쓰는 방식)
    // 장점1. final을 통해 상수로 사용 가능 (안정성 향상)
    // 장점2. 다형성 구현 가능
    // 장점3. 순환 참조 방지 (컴파일 타임에 check)
//    private final AuthorRepositoryInterface authorRepository;

    // 싱글톤 객체로 만들어지는 시점에 스프링에서 AuthorRepository 객체를 매개변수로 주입해 준다.
    // 생성자가 하나밖에 없을 때에는 @Autowired 생략가 가능
//    @Autowired
//    public AuthorService(AuthorMemoryRepository authorRepository) {
//        this.authorRepository = authorRepository;
//    }

    // ⭐방법3) @RequiredArgsConstructor 사용 -> 반드시 초기화되어야 하는 필드(final 등)를 대상으로 생성자를 자동 생성
    // 다형성 설계는 불가
//    private final AuthorMybatisRepository authorRepository;
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 회원 가입
    // 객체 조립은 서비스 담당
    public void save(AuthorCreateDTO authorCreateDTO, MultipartFile profileImage) {
        // 이메일 중복 검증
        if (authorRepository.findByEmail(authorCreateDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
//        this.authorRepository.save(회원객체);
        // 비밀번호 길이 검증
        if (authorCreateDTO.getPassword().length() <= 8) {
            throw new IllegalArgumentException("비밀번호가 너무 짧습니다.");
        }

        // 비밀번호 암호화
        String encodedPassoword = passwordEncoder.encode(authorCreateDTO.getPassword());
//        Author author = new Author(authorCreateDTO.getName(), authorCreateDTO.getEmail(), authorCreateDTO.getPassword());
        // toEntity 패턴을 통해 Author 객체 조립을 공통화
        Author author = authorCreateDTO.authorToEntity(encodedPassoword);
//        this.authorRepository.save(author);

        this.authorRepository.save(author);

        // 이미지 파일명 설정
        String fileName = "user-" + author.getId() + "-profileImage-" + profileImage.getOriginalFilename();

        // 저장 객체 구성
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(profileImage.getContentType())         // jpeg, mp4, ...
                .build();

        // 이미지 업로드 (byte 형태로 업로드)
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(profileImage.getBytes()));
        } catch (IOException e) {
            // checked 를 unchecked로 바꿔 전체 rollback 되도록 예외 처리
            throw new IllegalArgumentException("이미지 업로드 완료");
        }

        // S3에서 이미지 url 추출
        String imgUrl = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();

        // author 객체에 update
        author.updateImageUrl(imgUrl);

        // cascading 테스트 : 회원이 생성될 때, 곧바로 "가입 인사" 글을 생성하는 상황
        // 방법 1) 직접 Post 객체 생성 후 저장
        /*
        Post post = Post.builder()
                .title("안녕하세요")
                .contents(authorCreateDTO.getName() + "입니다. 반갑습니다.")
                .delYn("N")
                // author 객체가 db에 save 되는 순간 엔티티 매니저와 영속성 컨텍스트에 의해 author 객체에도 id값 생성
                .author(author)                // .author(author) 하게 되면 author 에는 id가 포함되어 있지 않음
                .build();
        */

//        postRepository.save(post);

        // 방법 2) cascade 옵션 활용
        // postRepository.save(post) 하지 않아도 저장되는 이유가 Author 엔티티 클래스에 postList 필드에 cascade 옵션을 걸어주었기 때문
//        author.getPostList().add(post);
        // post 빌더 패턴 위에 위치해도 됨. cascade 옵션을 설정했기 때문에 값이 변경되면 어차피 저장 후 매핑됨
//        this.authorRepository.save(author);
    }

    public Author doLogin(AuthorLoginDTO authorLoginDTO) {
        Optional<Author> optionalAuthor = authorRepository.findByEmail(authorLoginDTO.getEmail());

        boolean check = true;
        if (!optionalAuthor.isPresent()) {
            check = false;
        } else {
            // 비밀번호 일치 여부 검증 : matches 함수를 통해서 암호화되지 않은 값을 다시 암호화하여 db의 password를 검증
            if (!passwordEncoder.matches(authorLoginDTO.getPassword(), optionalAuthor.get().getPassword())) {
                check = false;
            }
        }

        if (!check) {
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다.");
        }

        return optionalAuthor.get();
    }

    @Transactional(readOnly = true)
    public List<AuthorListDTO> findAll() {
//        List<AuthorListDTO> dtoList = new ArrayList<>();
//        for (Author a : authorMemoryRepository.findAll()) {
//            AuthorListDTO dto = new AuthorListDTO(a.getId(), a.getName(), a.getEmail());
//            AuthorListDTO dto = author.listFromEntity();
//            dtoList.add(dto);
//        }
//        return dtoList;
        return authorRepository.findAll().stream().map(a -> a.listFromEntity()).collect(Collectors.toList());
//        return authorMemoryRepository.findAll();
    }

    // 회원 상세 조회 by id
//    public Author findById(Long id) throws NoSuchElementException {
//    NoSuchElementException : Collection 이나 Optional 객체의 요소 없을 시 발생
    @Transactional(readOnly = true)
    public AuthorDetailDTO findById(Long id) throws NoSuchElementException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 id 입니다."));
//        AuthorDetailDTO dto = new AuthorDetailDTO(author.getId(), author.getName(), author.getEmail());
//        AuthorDetailDTO dto1 = author.detailFromEntity();
        
        // 연관 관계 설정 없이 직접 조회하여 postCount 값 찾는 경우
        int postCount = postRepository.findByAuthorId(id).size();
        int postCount2 = postRepository.findByAuthor(author).size();
        AuthorDetailDTO dto2 = AuthorDetailDTO.fromEntity(author, postCount);

        // ⭐ @OneToMany 연관 관계 설정을 통해 postCount 값 찾는 경우
        AuthorDetailDTO dto3 = AuthorDetailDTO.fromEntity(author);
        return dto3;


        // optional 객체를 꺼내오는 것도 service 의 역할
        // 예외도 service 에서 발생시키는 이유 -> spring 에서 예외는 rollback의 기준이 되기 때문
        // service 에서 발생한 예외는 controller 에서 try-catch를 통해 예외 처리
//        Optional<Author> optionalAuthor = authorMemoryRepository.findById(id);
        // orElseThrow로 예외처리
//        return this.authorMemoryRepository.findById(id).orElseThrow();
//        return optionalAuthor.orElseThrow(() -> new NoSuchElementException("존재하지 않는 id 입니다."));
    }

    public AuthorDetailDTO myInfo() throws NoSuchElementException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 이메일입니다."));
        AuthorDetailDTO authorDetailDTO = AuthorDetailDTO.fromEntity(author);
        return authorDetailDTO;
    }

    // 비밀번호 변경
    public void updatePassword(AuthorUpdatePwDTO authorUpdatePwDTO) {
        // setter 없으니 수정 불가 -> Author 도메인에 메서드 생성
        Author author = authorRepository.findByEmail(authorUpdatePwDTO.getEmail())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이메일입니다."));
        // dirty checking : 객체를 수정한 후에 별도의 update 쿼리 발생시키지 않아도
        // 영속성 컨텍스트에 의해 객체 변경 사항 자동 DB 반영
        author.updatePw(authorUpdatePwDTO.getPassword());
    }

    // 회원 탈퇴
    public void delete(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
        authorRepository.delete(author);
//        authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
//        authorRepository.delete(id);
    }
}
