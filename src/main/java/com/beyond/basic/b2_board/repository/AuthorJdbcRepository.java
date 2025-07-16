package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorJdbcRepository {

    // DataSource는 DB와 JDBC에서 사용하는 DB 열결 드라이버 객체
    // application.yml에 설정한 DB 정보를 사용하여 dataSource 객체 싱글톤 생성
    @Autowired
    private DataSource dataSource;

    // jdbc의 단점
    // 단점 1) raw 쿼리에서 오타가 나도 디버깅 어려움
    // 단점 2) 데이터 추가 시 매개변수와 컬럼의 매핑을 수작업 해야 함 (insert 시)
    // 단점 3) 데이터 조회 시 객체 조립을 직접해야 함
    public void save(Author author) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "insert into author(name, email, password) values(?, ?, ?)";
            // PreparedStatement 객체로 만들어서 실행 가능한 상태로 만드는 것
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, author.getName());
            ps.setString(2, author.getEmail());
            ps.setString(3, author.getPassword());
//            ps.executeQuery();          // 조회
            ps.executeUpdate();         // 추가/수정
        } catch (SQLException e) {
            // transaction 상황에서 unchecked 예외는 spring 에서 롤백의 기준이 된다
            throw new RuntimeException(e);
        }

    }

    // 회원 목록 조회
    public List<Author> findAll() {
        List<Author> authorList = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from author";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                Author author = new Author(id, name, email, password);
                authorList.add(author);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
        return authorList;      // 데이터 없으면 [] return
    }

    // 회원 상세 조회 by id
    public Optional<Author> findById(Long inputId) {
        Author author = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from author where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setLong(1, inputId);
            // ResultSet 테이블 형태의 데이터
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {      // 데이터 (값이 없으면 null 반환)

                // 데이터 꺼내기
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");

                // 재조립
                author = new Author(id, name, email, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(author);
    }

    // 회원 상세 조회 by email
    public Optional<Author> findByEmail(String inputEmail) {
        Author author = null;
        try {
            Connection connection = dataSource.getConnection();
            String sql = "select * from author where email = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, inputEmail);
            // ResultSet 테이블 형태의 데이터
            ResultSet rs = ps.executeQuery();

            // 신규 사용자는 데이터가 없기 때문에 error 발생하기 때문에 if 문으로 체크해주어야 함
            if (rs.next()) {        // rs.next() 하는 순간 포인터가 컬럼에서 첫번째 데이터로 바뀜
                // 데이터 꺼내기
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");

                // 재조립
                author = new Author(id, name, email, password);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(author);
    }

    // 회원 정보(비밀번호) 수정


    // 회원 탈퇴
    public void delete(Long id) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "delete from author where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
//            ps.executeQuery();          // 조회
            ps.executeUpdate();         // 추가/수정/삭제
        } catch (SQLException e) {
            // transaction 상황에서 unchecked 예외는 spring 에서 롤백의 기준이 된다
            throw new RuntimeException(e);
        }
    }
}
