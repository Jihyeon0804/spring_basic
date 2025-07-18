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
public class PostDetailDTO {

    private Long id;
    private String title;
    private String contents;
    private String authorEmail;

    // 엔티티 간 관계성 설정을 하지 않았을 경우
//    public static PostDetailDTO fromEntity(Post post, Author author) {
//        return PostDetailDTO.builder()
//                .id(post.getId())
//                .title(post.getTitle())
//                .contents(post.getContents())
//                .authorEmail(author.getEmail())
//                .build();
//    }
    
    // 엔티티 간 관계성 설정한 경우
    public static PostDetailDTO fromEntity(Post post) {
        return PostDetailDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(post.getAuthor().getEmail())
                .build();
    }
}
