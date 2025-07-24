package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostCreateDTO {

    @NotEmpty
    private String title;

    private String contents;

    @Builder.Default
    private String appointment = "N";

    // 시간 정보는 직접 LocalDateTime 으로 형변환하는 경우가 많음
    private String appointmentTime;         // LocalDateTime 으로 받는 것보다는 String 으로 받는 것이 편함(사용자에게 받을 때도 String임)

    /*
    @NotNull                    // 숫자는 @NotEmpty 사용 불가
    private Long authorId;
     */


    public Post toEntity(Author author, LocalDateTime appointmentTime) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                .author(author)
                .appointment(this.appointment)
                .appointmentTime(appointmentTime)
                .delYn("N")
                .build();
    }
}
