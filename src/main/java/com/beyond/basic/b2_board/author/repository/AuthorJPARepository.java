package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorJPARepository {

    @Autowired
    private EntityManager entityManager;

    public void save(Author author) {
        // persist() : 순수 jpa 에서 데이터를 insert 하는 메서드
        entityManager.persist(author);
    }

    public List<Author> findAll() {
        // 순수 jpa 에서는 제한된 메서드 제공으로 jpql을 사용하여 직접 쿼리 작성하는 경우 많음
        // jpql은 순수 raw query와 다름. jpql 문법은 문자열 형식의 raw query가 아닌 객체 지향 query 문
        // jpql 작성 규칙 : db 테이블명/컬럼명이 아니라, 엔티티명/필드명을 기준으로 사용하고, 별칭(alias)를 활용
        // select 별칭 from Entity명 별칭
        // 특정 컬럼 명시하고 싶다면 별칭.컬럼명
        // findAll은 제공X
        List<Author> authorList = entityManager.createQuery("select a from Author a", Author.class).getResultList();
        return authorList;
    }

    public Optional<Author> findById(Long id) {
        // find(return 타입, 매개변수)
        // find 는 pk로 조회하는 경우에 select문 자동 완성
        Author author = entityManager.find(Author.class, id);       // 파라미터가 pk만 가능
        return Optional.ofNullable(author);
    }

    public Optional<Author> findByEmail(String email) {
        Author author = null;
        try {
            // 존재하지 않으면 NoSuchElementException 발생 (null이 담기지 않음)
            author = entityManager.createQuery("select a from Author a where a.email = :email", Author.class)
                    .setParameter("email", email).getSingleResult();
        } catch (Exception e) {
        }

        return Optional.ofNullable(author);
    }

    public  void delete(Long id) {
        // remove(객체) : 객체 삭제
        Author author = entityManager.find(Author.class, id);
        if (author != null) {
            entityManager.remove(author);
        }
    }

}
