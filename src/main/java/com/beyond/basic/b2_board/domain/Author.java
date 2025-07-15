package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.dto.AuthorDetailDTO;
import com.beyond.basic.b2_board.dto.AuthorListDTO;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Author {

    private Long id;
    private String name;
    private String email;
    private String password;

    public Author(String name, String email, String password) {
//        this.id = AuthorMemoryRepository.id;          // db 연결 시 필요X
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updatePw(String password) {
        this.password = password;
    }

    public AuthorDetailDTO detailFromEntity() {
        return new AuthorDetailDTO(this.id, this.name, this.email);
    }

    public AuthorListDTO listFromEntity() {
        return new AuthorListDTO(this.id, this.name, this.email);
    }
}
