package com.backjoongwon.cvi.post.dto;

import com.backjoongwon.cvi.user.dto.UserResponse;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PostResponse {

    private Long id;
    private UserResponse user;
    private String content;
    private int viewCount;
    private String vaccinationType;
    private LocalDateTime createdAt;

    public PostResponse(Long id, UserResponse user, String content, int viewCount, String vaccinationType, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.viewCount = viewCount;
        this.vaccinationType = vaccinationType;
        this.createdAt = createdAt;
    }

    public PostResponse(Long id) {
        this(id, null, null, 0, null, null);
    }
}
