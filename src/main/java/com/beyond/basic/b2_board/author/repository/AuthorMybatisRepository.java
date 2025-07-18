package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper     // mybatis repository 로 만들 때 필요한 어노테이션
public interface AuthorMybatisRepository {

    void save(Author author);

    List<Author> findAll();

    Optional<Author> findById(Long id);

    Optional<Author> findByEmail(String email);
    
    void delete(Long id);
}
