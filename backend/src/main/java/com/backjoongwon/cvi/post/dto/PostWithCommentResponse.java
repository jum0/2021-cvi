package com.backjoongwon.cvi.post.dto;

import com.backjoongwon.cvi.comment.domain.Comment;
import com.backjoongwon.cvi.comment.dto.CommentResponse;
import com.backjoongwon.cvi.post.domain.Post;
import com.backjoongwon.cvi.post.domain.VaccinationType;
import com.backjoongwon.cvi.user.domain.User;
import com.backjoongwon.cvi.user.dto.UserResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostWithCommentResponse {

    private Long id;
    private UserResponse writer;
    private String content;
    private int viewCount;
    private int likeCount;
    private boolean hasLiked;
    private List<CommentResponse> comments;
    private VaccinationType vaccinationType;
    private LocalDateTime createdAt;
    private List<String> images;

    public PostWithCommentResponse(Long id, UserResponse user, String content, int viewCount, int likeCount,
                                   boolean hasLiked, List<CommentResponse> comments, VaccinationType vaccinationType, LocalDateTime createdAt, List<String> imageUrls) {
        this.id = id;
        this.writer = user;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.hasLiked = hasLiked;
        this.comments = comments;
        this.vaccinationType = vaccinationType;
        this.createdAt = createdAt;
        this.images = imageUrls;
    }

    public static PostWithCommentResponse of(Post post, User viewer) {
        return new PostWithCommentResponse(post.getId(), UserResponse.of(post.getUser(), null), post.getContent(),
                post.getViewCount(), post.getLikesCount(), post.isAlreadyLikedBy(viewer),
                makeCommentResponses(post.getCommentsAsList()), post.getVaccinationType(), post.getCreatedAt(), post.getImagesAsUrlList());
    }

    private static List<CommentResponse> makeCommentResponses(List<Comment> comments) {
        return comments.stream()
                .map(CommentResponse::of)
                .collect(Collectors.toList());
    }

    public static List<PostWithCommentResponse> toList(List<Post> posts, User viewer) {
        return posts.stream()
                .map(post -> PostWithCommentResponse.of(post, viewer))
                .collect(Collectors.toList());
    }
}
