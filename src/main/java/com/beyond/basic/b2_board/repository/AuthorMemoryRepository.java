package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorDetailDTO;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
//public class AuthorMemoryRepository implements AuthorRepositoryInterface {
public class AuthorMemoryRepository {

    private List<Author> authorList = new ArrayList<>();
    public static Long id = 1L;

    // 회원 가입
    public void save(Author author) {
        this.authorList.add(author);
        id++;
    }

    // 회원 목록 조회
    public List<Author> findAll() {
        return this.authorList;
    }

    // 회원 상세 조회 by id
    public Optional<Author> findById(Long id) {
        Author author = null;
        for (Author a : this.authorList) {
            if (a.getId().equals(id)) {
                author = a;
            }
        }
        return Optional.ofNullable(author);

        //        return authorList.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    // 회원 상세 조회 by email
    public Optional<Author> findByEmail(String email) {
        Author author = null;
        for (Author a : this.authorList) {
            if (a.getEmail().equals(email)) {
                author = a;
            }
        }
        return Optional.ofNullable(author);
//        return authorList.stream().filter(a -> a.getEmail().equals(email)).findFirst();
    }

    // 회원 정보 수정 repository에서 메서드 필요X (서비스 계층에서 진행)

    // 회원 탈퇴
    public void delete(Long id) {
        // id 값으로 요소를 찾아서 삭제
        for (int i = 0; i < this.authorList.size(); i++) {
            if (Objects.equals(this.authorList.get(i).getId(), id)) {
                this.authorList.remove(i);
                break;
            }
        }
    }
}
