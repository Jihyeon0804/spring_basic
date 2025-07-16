package com.beyond.basic.b2_board.dto;

import com.beyond.basic.b2_board.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDetailDTO {

    private Long id;
    private String name;
    private String email;
    
    // 한 개의 Entity로만 DTO가 조립되는 것이 아니기 때문에 DTO 계층에서 fromEntity 설계
    // 그 외 필요한 데이터가 있다면 파라미터로 여러 개 받아서 조립해서 return
    public static AuthorDetailDTO fromEntity(Author author) {
        return new AuthorDetailDTO(author.getId(), author.getName(), author.getEmail());
    }

}
