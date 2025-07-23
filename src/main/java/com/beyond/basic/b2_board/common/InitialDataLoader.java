package com.beyond.basic.b2_board.common;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// CommandLineRunner 를 구현함으로서 해당 컴포넌트가 스프링 빈(싱글톤 객체)으로 등록되는 시점에 run 메서드 자동 실행
// 자동화 작업이 필요할 때 사용
@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {

        if (authorRepository.findByEmail("admin@email.com").isPresent()) {
            return;
        }

        Author author = Author.builder()
                .email("admin@email.com")
                .role(Role.ADMIN)
                .password(passwordEncoder.encode("123412341234"))
                .build();

        authorRepository.save(author);
    }
}
