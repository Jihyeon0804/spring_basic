package com.beyond.basic.b2_board.post.dto;


import com.beyond.basic.b2_board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostListDTO {

    private Long id;
    private String title;
    private String authorEmail;

    public static PostListDTO fromEntity(Post post) {
        return PostListDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .authorEmail(post.getAuthor().getEmail())
                .build();
    }

}
