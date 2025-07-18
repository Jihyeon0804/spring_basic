package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.author.dto.AuthorDetailDTO;
import com.beyond.basic.b2_board.author.dto.AuthorListDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
    // 컬럼에 별다른 설정이 없을 경우 default varchar(255)
    private String name;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
//    @Column(name = "pw")  :   되도록이면 컬럼명과 변수명을 일치시키는 것이 개발의 혼선을 줄일 수 있음.
    private String password;
    // 컬럼명에 캐멀 케이스 사용 시, db에는 created_time으로 컬럼 생성
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updatedTime;
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
