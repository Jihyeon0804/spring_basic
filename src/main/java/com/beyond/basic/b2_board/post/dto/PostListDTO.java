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
    private String category;
    private String title;
    private String contents;
    private String authorEmail;

    public static PostListDTO fromEntity(Post post) {
        return PostListDTO.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(post.getAuthor().getEmail())
                .build();
    }

}
