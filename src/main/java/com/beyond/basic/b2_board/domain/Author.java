package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    private Long id;
    private String name;
    private String email;
    private String password;

    public Author(String name, String email, String password) {
        this.id = AuthorMemoryRepository.id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updatePw(String password) {
        this.password = password;
    }
}
