package com.beyond.basic.b2_board.service;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDTO;
import com.beyond.basic.b2_board.dto.AuthorDetailDTO;
import com.beyond.basic.b2_board.dto.AuthorListDTO;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDTO;
//import com.beyond.basic.b2_board.repository.AuthorJdbcRepository;
import com.beyond.basic.b2_board.repository.AuthorMybatisRepository;
import com.beyond.basic.b2_board.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Transactional      // 스프링에서 메서드 단위로 트랜잭션 처리하고, 만약 예외(unchecked) 발생 시 자동 롤백 처리 지원
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

    // 회원 가입
    // 객체 조립은 서비스 담당
    public void save(AuthorCreateDTO authorCreateDTO) {
        // 이메일 중복 검증
        if (authorRepository.findByEmail(authorCreateDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
//        this.authorRepository.save(회원객체);
        // 비밀번호 길이 검증
//        if (authorCreateDTO.getPassword().length() <= 8) {
//            throw new IllegalArgumentException("비밀번호가 너무 짧습니다.");
//        }

//        Author author = new Author(authorCreateDTO.getName(), authorCreateDTO.getEmail(), authorCreateDTO.getPassword());
        // toEntity 패턴을 통해 Author 객체 조립을 공통화
        Author author = authorCreateDTO.authorToEntity();
        this.authorRepository.save(author);
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
        AuthorDetailDTO dto2 = AuthorDetailDTO.fromEntity(author);
        return dto2;


        // optional 객체를 꺼내오는 것도 service 의 역할
        // 예외도 service 에서 발생시키는 이유 -> spring 에서 예외는 rollback의 기준이 되기 때문
        // service 에서 발생한 예외는 controller 에서 try-catch를 통해 예외 처리
//        Optional<Author> optionalAuthor = authorMemoryRepository.findById(id);
        // orElseThrow로 예외처리
//        return this.authorMemoryRepository.findById(id).orElseThrow();
//        return optionalAuthor.orElseThrow(() -> new NoSuchElementException("존재하지 않는 id 입니다."));
    }

    // 비밀번호 변경
    public void updatePassword(AuthorUpdatePwDTO authorUpdatePwDTO) {
        // setter 없으니 수정 불가 -> Author 도메인에 메서드 생성
        Author author = authorRepository.findByEmail(authorUpdatePwDTO.getEmail())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이메일입니다."));
        // dirty checking : 객체를 수정한 후에 별도의 update 쿼리 발생시키지 않아도
        // 영속성 컨텍스트에 의해 객체 변경 사항 자동 DB 반영
        author.updatePw(authorUpdatePwDTO.getPassword());
        System.out.println(author.getPassword());
    }

    // 회원 탈퇴
    public void delete(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
        authorRepository.delete(author);
//        authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 사용자입니다."));
//        authorRepository.delete(id);
    }
}
