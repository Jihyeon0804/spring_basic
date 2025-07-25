package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.author.dto.AuthorListDTO;
import com.beyond.basic.b2_board.common.BaseTimeEntity;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// JPA를 사용할 경우 @Entity를 반드시 붙어야하는 어노테이션 (매핑할 기준이 되는 Entity 설정을 위해 사용)
// JPA의 EntityManager 에게 객체를 위임하기 위한 어노테이션
// EntityManager는 영속성 컨텍스트(엔티티의 현재 상황)를 통해 DB 데이터 관리
// @Builder를 통해 유연하게 객체 생성 가능 (@AllArgsConstructor 반드시 있어야 함)
@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Author extends BaseTimeEntity {

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
    @Enumerated(EnumType.STRING)        // db의 문자열 형태로 저장
    @Builder.Default                    // 빌터 패턴에서 변수 초기화 (디폴트 값) 설정 시 @Builder.Default 필수
    private Role role = Role.USER;      // 기본값을 user로 설정, 값이 할당되면 해당 값으로 세팅

    private String profileImage;

    // @OneToMany는 완전한 선택 사항, @ManyToOne과 달리 fetch 옵션의 default가 FetchType.LAZY
    // mappedBy 에는 ManyToOne 쪽에 변수명을 문자열로 지정 (Post 엔티티 객체에 있는 Author 필드의 변수명)
    // mappedBy를 지정해야 하는 이유는 FK 관리를 매핑되어 있는 (Post) 쪽에서 한다는 의미 => 연관 관계의 주인 설정
    // cascade : 부모 객체의 변화에 따라 자식 객체가 같이 변하는 옵션 1) CascadeType.PERSIST : 저장(생성) 2) CascadeType.REMOVE : 삭제(물리)
    // orphanRemoval = true : 자식의 자식까지(연쇄적으로 이어져 있는 경우) 모두 삭제하는 경우
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    // @OneToMany 설정 시 List 초기화 필수(postList.add()하는 부분에서 NullPointerException 발생 가능, @Builder.Default 설정 필수
    List<Post> postList = new ArrayList<>();

    // mappedBy는 Address 엔티티 객체에 있는 Author의 필드의 변수명 작성 (Author 엔티티 객체를 의미하는 게 아님)
    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;
    
    // BaseTimeEntity에 공통화 시킴
//    // 컬럼명에 캐멀 케이스 사용 시, db 에는 created_time 으로 컬럼 생성
//    @CreationTimestamp
//    private LocalDateTime createdTime;
//    @UpdateTimestamp
//    private LocalDateTime updatedTime;
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

    public AuthorListDTO listFromEntity() {
        return new AuthorListDTO(this.id, this.name, this.email);
    }

    public void updateImageUrl(String imageUrl) {
        this.profileImage = imageUrl;
    }
}
