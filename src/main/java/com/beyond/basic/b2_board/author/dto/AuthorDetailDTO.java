package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDetailDTO {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private int postCount;
    private LocalDateTime createdTime;

    // 한 개의 Entity로만 DTO가 조립되는 것이 아니기 때문에 DTO 계층에서 fromEntity 설계
    // 그 외 필요한 데이터가 있다면 파라미터로 여러 개 받아서 조립해서 return
    public static AuthorDetailDTO fromEntity(Author author, int postCount) {
        return AuthorDetailDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .role(author.getRole())
                .postCount(postCount)
                .createdTime(author.getCreatedTime()).build();
    }

    public static AuthorDetailDTO fromEntity(Author author) {
        return AuthorDetailDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .role(author.getRole())
                .postCount(author.getPostList().size())
                .createdTime(author.getCreatedTime()).build();
    }

}
