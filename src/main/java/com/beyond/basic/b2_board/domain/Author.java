package com.beyond.basic.b2_board.domain;

import com.beyond.basic.b2_board.dto.AuthorDetailDTO;
import com.beyond.basic.b2_board.dto.AuthorListDTO;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

// JPA를 사용할 경우 @Entity를 반드시 붙어야하는 어노테이션 (매핑할 기준이 되는 Entity 설정을 위해 사용)
// JPA의 EntityManager 에게 객체를 위임하기 위한 어노테이션
// EntityManager는 영속성 컨텍스트(엔티티의 현재 상황)를 통해 DB 데이터 관리
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Author {

    // 해당 컬럼 PK 설정
    @Id
    // GenerationType.IDENTITY : auto_increment, GenerationType.AUTO : id 생성 전략을 jpa 에게 자동 설정하도록 위임하는 것
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
//    private String test;
//    private String test2;

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
