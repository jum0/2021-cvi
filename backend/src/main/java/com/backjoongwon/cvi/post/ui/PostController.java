package com.backjoongwon.cvi.post.ui;

import com.backjoongwon.cvi.comment.dto.CommentRequest;
import com.backjoongwon.cvi.comment.dto.CommentResponse;
import com.backjoongwon.cvi.post.application.PostService;
import com.backjoongwon.cvi.post.domain.VaccinationType;
import com.backjoongwon.cvi.post.dto.PostRequest;
import com.backjoongwon.cvi.post.dto.PostResponse;
import com.backjoongwon.cvi.user.auth.AuthenticationPrincipal;
import com.backjoongwon.cvi.user.domain.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@AuthenticationPrincipal RequestUser user, @RequestBody PostRequest postRequest, HttpServletResponse servletResponse) {
        PostResponse postResponse = postService.create(user.getId(), postRequest);
        servletResponse.setHeader("Location", "/api/v1/posts/" + postResponse.getId());
        return postResponse;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponse> findByVaccineType(@RequestParam(defaultValue = "ALL") VaccinationType vaccinationType) {
        return postService.findByVaccineType(vaccinationType);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse find(@PathVariable Long id) {
        return postService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @AuthenticationPrincipal RequestUser user, @RequestBody PostRequest postRequest) {
        postService.update(id, user, postRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal RequestUser user) {
        postService.delete(id, user);
    }

    @PostMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(@PathVariable Long postId,
                                         @AuthenticationPrincipal RequestUser user,
                                         @RequestBody CommentRequest commentRequest,
                                         HttpServletResponse servletResponse) {
        CommentResponse commentResponse = postService.createComment(postId, user, commentRequest);
        servletResponse.setHeader("Location", "/api/v1/comments/" + commentResponse.getId());
        return commentResponse;
    }
}
