package com.backjoongwon.cvi.post.ui;

import com.backjoongwon.cvi.ApiDocument;
import com.backjoongwon.cvi.auth.domain.authorization.SocialProvider;
import com.backjoongwon.cvi.comment.domain.Comment;
import com.backjoongwon.cvi.comment.dto.CommentRequest;
import com.backjoongwon.cvi.comment.dto.CommentResponse;
import com.backjoongwon.cvi.common.exception.NotFoundException;
import com.backjoongwon.cvi.common.exception.UnAuthorizedException;
import com.backjoongwon.cvi.post.application.PostService;
import com.backjoongwon.cvi.post.domain.VaccinationType;
import com.backjoongwon.cvi.post.dto.LikeResponse;
import com.backjoongwon.cvi.post.dto.PostRequest;
import com.backjoongwon.cvi.post.dto.PostResponse;
import com.backjoongwon.cvi.user.application.UserService;
import com.backjoongwon.cvi.user.domain.AgeRange;
import com.backjoongwon.cvi.user.domain.JwtTokenProvider;
import com.backjoongwon.cvi.user.domain.User;
import com.backjoongwon.cvi.user.dto.AgeRangeResponse;
import com.backjoongwon.cvi.user.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("게시글 컨트롤러 Mock 테스트")
@WebMvcTest(controllers = PostController.class)
class PostControllerTest extends ApiDocument {

    private static final Long USER_ID = 1L;
    private static final Long POST_ID = 1L;
    private static final Long LIKE_ID = 1L;
    private static final String ACCESS_TOKEN = "{ACCESS TOKEN}";
    private static final Long COMMENT_ID = 1L;
    private static final String BEARER = "Bearer ";

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private User user;
    private User anotherUser;
    private PostRequest request;
    private UserResponse userResponse;
    private List<CommentResponse> commentResponses;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(USER_ID)
                .nickname("user")
                .ageRange(AgeRange.TEENS)
                .profileUrl("naver.com/profile")
                .socialId("{Unique ID received from social provider}")
                .socialProvider(SocialProvider.NAVER)
                .build();
        anotherUser = User.builder()
                .id(USER_ID + 1)
                .nickname("another_user")
                .ageRange(AgeRange.TWENTIES)
                .profileUrl("kakao.com/profile")
                .socialId("{Unique ID received from social provider}")
                .socialProvider(SocialProvider.KAKAO)
                .build();

        request = new PostRequest("글 내용", VaccinationType.PFIZER);
        userResponse = UserResponse.of(user, null);

        Comment comment1 = Comment.builder().id(COMMENT_ID).content("댓글1").user(user).createdAt(LocalDateTime.now()).build();
        Comment comment2 = Comment.builder().id(COMMENT_ID + 1).content("댓글2").user(anotherUser).createdAt(LocalDateTime.now()).build();

        commentResponses = Arrays.asList(CommentResponse.of(comment1), CommentResponse.of(comment2));

        given(jwtTokenProvider.isValidToken(ACCESS_TOKEN)).willReturn(true);
        given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn(String.valueOf(user.getId()));
        given(userService.findUserById(any(Long.class))).willReturn(user);
    }

    @DisplayName("게시글 등록 - 성공")
    @Test
    void createPost() throws Exception {
        //given
        PostResponse expectedResponse = new PostResponse(POST_ID, userResponse, request.getContent(), 0, 0, false, Collections.emptyList(), request.getVaccinationType(), LocalDateTime.now());
        given(postService.create(any(), any(PostRequest.class))).willReturn(expectedResponse);
        //when
        ResultActions response = 글_등록_요청(request);
        //then
        글_등록_성공함(response, expectedResponse);
    }

    @DisplayName("게시글 등록 - 실패")
    @Test
    void createPostFailure() throws Exception {
        //given
        willThrow(new NotFoundException("해당 id의 사용자가 존재하지 않습니다.")).given(postService).create(any(), any(PostRequest.class));
        //when
        ResultActions response = 글_등록_요청(request);
        //then
        글_등록_실패함(response);
    }

    @DisplayName("게시글 단일 조회 - 성공")
    @Test
    void find() throws Exception {
        //given
        PostResponse expectedPostResponse = new PostResponse(POST_ID, userResponse, "글 내용", 1, 0, false, commentResponses, VaccinationType.PFIZER, LocalDateTime.now());
        given(postService.findById(any(Long.class), any())).willReturn(expectedPostResponse);
        //when
        ResultActions response = 글_단일_조회_요청(POST_ID);
        //then
        글_단일_조회_성공함(response, expectedPostResponse);
    }

    @DisplayName("게시글 단일 조회 - 실패")
    @Test
    void findFailure() throws Exception {
        //given
        willThrow(new NotFoundException("해당 id의 게시글이 존재하지 않습니다.")).given(postService).findById(any(Long.class), any());
        //when
        ResultActions response = 글_단일_조회_요청(POST_ID);
        //then
        글_단일_조회_실패함(response);
    }

    @DisplayName("게시글 전체 조회 - 성공")
    @Test
    void findAll() throws Exception {
        //given
        UserResponse anotherUserResponse = UserResponse.of(user, null);

        List<PostResponse> postResponses = new LinkedList<>(Arrays.asList(
                new PostResponse(POST_ID + 1, anotherUserResponse, "글 내용2", 12, 0, false, commentResponses, VaccinationType.MODERNA, LocalDateTime.now()),
                new PostResponse(POST_ID, userResponse, "글 내용1", 55, 5, true, Collections.emptyList(), VaccinationType.PFIZER, LocalDateTime.now().minusDays(1L))
        ));
        willReturn(postResponses).given(postService).findByVaccineType(any(VaccinationType.class), any());
        //when
        ResultActions response = 글_전체_조회_요청();
        //then
        글_전체_조회_성공함(response, postResponses);
    }

    @DisplayName("게시글 전체 조회 - 성공 - 게시글이 하나도 없는 경우")
    @Test
    void findAllWhenPostsIsEmpty() throws Exception {
        //given
        List<PostResponse> postResponses = Collections.emptyList();
        willReturn(postResponses).given(postService).findByVaccineType(any(VaccinationType.class), any());
        //when
        ResultActions response = 글_전체_조회_요청();
        //then
        글_전체_조회_성공함_게시글없음(response, postResponses);
    }

    @DisplayName("게시글 수정 - 성공")
    @Test
    void updatePost() throws Exception {
        //given
        willDoNothing().given(postService).update(any(Long.class), any(), any(PostRequest.class));
        //when
        ResultActions response = 글_수정_요청(POST_ID, request);
        //then
        글_수정_성공함(response);
    }

    @DisplayName("게시글 수정 - 실패")
    @Test
    void updatePostFailure() throws Exception {
        //given
        willThrow(new NotFoundException("해당 id의 게시글이 존재하지 않습니다.")).given(postService).update(any(Long.class), any(), any(PostRequest.class));
        //when
        ResultActions response = 글_수정_요청(POST_ID, request);
        //then
        글_수정_실패함(response);
    }

    @DisplayName("게시글 삭제 - 성공")
    @Test
    void deletePost() throws Exception {
        //given
        willDoNothing().given(postService).delete(any(Long.class), any());
        //when
        ResultActions response = 글_삭제_요청(POST_ID);
        //then
        글_삭제_성공함(response);
    }

    @DisplayName("게시글 삭제 - 실패")
    @Test
    void deletePostFailure() throws Exception {
        //given
        willThrow(new NotFoundException("해당 id의 게시글이 존재하지 않습니다.")).given(postService).delete(any(Long.class), any());
        //when
        ResultActions response = 글_삭제_요청(POST_ID);
        //then
        글_삭제_실패함(response);
    }

    @DisplayName("게시글 타입별 조회 - 성공")
    @Test
    void findByVaccineType() throws Exception {
        //given
        List<PostResponse> postResponses = new LinkedList<>(Arrays.asList(
                new PostResponse(3L, userResponse, "이건 내용입니다.", 100, 10, true, commentResponses, VaccinationType.PFIZER, LocalDateTime.now()),
                new PostResponse(2L, userResponse, "이건 내용입니다.2", 200, 20, false, Collections.emptyList(), VaccinationType.PFIZER, LocalDateTime.now()),
                new PostResponse(1L, userResponse, "이건 내용입니다.3", 300, 30, true, Collections.emptyList(), VaccinationType.PFIZER, LocalDateTime.now())
        ));
        willReturn(postResponses).given(postService).findByVaccineType(any(VaccinationType.class), any());
        //when
        ResultActions response = 글_타입별_조회_요청(VaccinationType.PFIZER);
        //then
        글_타입별_조회_요청_성공함(response);
    }

    @DisplayName("게시글 타입별 조회 - 성공 - 게시글이 하나도 없는 경우")
    @Test
    void findByVaccineTypeWhenPostsIsEmpty() throws Exception {
        //given
        List<PostResponse> postResponses = Collections.emptyList();
        willReturn(postResponses).given(postService).findByVaccineType(any(VaccinationType.class), any());
        //when
        ResultActions response = 글_타입별_조회_요청(VaccinationType.PFIZER);
        //then
        글_타입별_조회_성공함_게시글없음(response, postResponses);
    }

    @DisplayName("게시글 타입별 조회 페이징 - 성공")
    @Test
    void findByVaccineTypePaging() throws Exception {
        //given
        List<PostResponse> postResponses = new LinkedList<>(Arrays.asList(
                new PostResponse(38L, userResponse, "이건 내용입니다.", 100, 10, true, commentResponses, VaccinationType.PFIZER, LocalDateTime.now()),
                new PostResponse(37L, userResponse, "이건 내용입니다.2", 200, 20, false, Collections.emptyList(), VaccinationType.PFIZER, LocalDateTime.now()),
                new PostResponse(36L, userResponse, "이건 내용입니다.3", 300, 30, true, Collections.emptyList(), VaccinationType.PFIZER, LocalDateTime.now())
        ));
        willReturn(postResponses).given(postService).findByVaccineType(any(VaccinationType.class), anyLong(), anyInt(), any());
        //when
        ResultActions response = 글_타입별_페이징_조회_요청(VaccinationType.PFIZER, 39L, 3);
        //then
        글_타입별_페이징_조회_요청_성공함(response);
    }

    @DisplayName("게시글 타입별 페이징 조회 - 성공 - 게시글이 하나도 없는 경우")
    @Test
    void findByVaccineTypePagingWhenPostsIsEmpty() throws Exception {
        //given
        List<PostResponse> postResponses = Collections.emptyList();
        willReturn(postResponses).given(postService).findByVaccineType(any(VaccinationType.class), anyLong(), anyInt(), any());
        //when
        ResultActions response = 글_타입별_페이징_조회_요청(VaccinationType.PFIZER, 0L, 3);
        //then
        글_타입별_페이징_조회_요청_성공함_게시글없음(response, postResponses);
    }

    @DisplayName("게시글 좋아요 생성 - 성공")
    @Test
    void createLike() throws Exception {
        //given
        PostResponse expectedPostResponse = createPostResponse();
        LikeResponse likeResponse = LikeResponse.from(1L);
        willReturn(likeResponse).given(postService).createLike(any(Long.class), any());
        //when
        ResultActions actualResponse = 글_좋아요_생성_요청(expectedPostResponse.getId());
        //then
        글_좋아요_생성_성공(actualResponse, 1L);
    }

    @DisplayName("게시글 좋아요 생성 - 실패 - 게시글이 없는 경우")
    @Test
    void createLikeFailureWhenPostNotExists() throws Exception {
        //given
        PostResponse expectedPostResponse = createPostResponse();
        willThrow(new NotFoundException("해당 id의 게시글이 존재하지 않습니다.")).given(postService).createLike(any(Long.class), any());
        //when
        ResultActions response = 글_좋아요_생성_요청(expectedPostResponse.getId());
        //then
        글_좋아요_생성_실패(response);
    }

    @DisplayName("게시글 좋아요 삭제 - 성공")
    @Test
    void deleteLike() throws Exception {
        //given
        willDoNothing().given(postService).delete(any(Long.class), any());
        //when
        ResultActions response = 글_좋아요_삭제_요청();
        //then
        글_좋아요_삭제_성공함(response);
    }

    @DisplayName("게시글 좋아요 삭제 - 실패 - 토큰이 유효하지 않은 경우")
    @Test
    void deleteLikeFailureWhenNotTokenValid() throws Exception {
        //given
        willThrow(new UnAuthorizedException("유효하지 않은 토큰입니다."))
                .given(postService).deleteLike(any(Long.class), any());
        //when
        ResultActions response = 글_좋아요_삭제_요청();
        //then
        글_좋아요_삭제_실패함(response);
    }

    @DisplayName("게시글 댓글 등록 - 성공")
    @Test
    void createComment() throws Exception {
        //given
        CommentResponse expectedResponse = new CommentResponse(1L, userResponse, "좋은 정보 공유 감사해요 ㅎㅎㅎ", LocalDateTime.now());
        willReturn(expectedResponse).given(postService).createComment(anyLong(), any(), any(CommentRequest.class));
        //when
        ResultActions response = 댓글_등록_요청(POST_ID, new CommentRequest("좋은 정보 공유 감사해요 ㅎㅎㅎ"), BEARER + ACCESS_TOKEN);
        //then
        댓글_등록_성공함(response, expectedResponse);
    }

    @DisplayName("게시글 댓글 등록 - 실패 - 비회원이 댓글을 작성할 때")
    @Test
    void createCommentFailureWhenWrongWriter() throws Exception {
        //given
        willThrow(new UnAuthorizedException("가입된 유저가 아닙니다.")).given(postService).createComment(anyLong(), any(), any(CommentRequest.class));
        //when
        ResultActions response = 댓글_등록_요청(POST_ID, new CommentRequest("좋은 정보 공유 감사해요 ㅎㅎㅎ"), "null");
        //then
        댓글_등록_실패함(response);
    }

    @DisplayName("게시글 댓글 수정 - 성공")
    @Test
    void putComment() throws Exception {
        //given
        CommentRequest updateRequest = new CommentRequest("수정된 좋은 정보 공유 감사해요 ㅎㅎ");
        willDoNothing().given(postService).updateComment(anyLong(), anyLong(), any(), any(CommentRequest.class));
        //when
        ResultActions response = 댓글_수정_요청(POST_ID, COMMENT_ID, updateRequest, BEARER + ACCESS_TOKEN);
        //then
        댓글_수정_성공함(response);
    }

    @DisplayName("게시글 댓글 수정 - 실패 - 작성자가 아닌 사용자가 수정 요청")
    @Test
    void putCommentFailureWhenWrongUser() throws Exception {
        //given
        CommentRequest updateRequest = new CommentRequest("수정된 좋은 정보 공유 감사해요 ㅎㅎ");
        willThrow(new UnAuthorizedException("댓글 작성자가 아닙니다.")).given(postService).updateComment(anyLong(), anyLong(),
                any(), any(CommentRequest.class));
        //when
        ResultActions response = 댓글_수정_요청(POST_ID, COMMENT_ID, updateRequest, BEARER + "another_user_token");
        //then
        댓글_수정_실패함(response);
    }

    @DisplayName("게시글 댓글 삭제 - 성공")
    @Test
    void deleteComment() throws Exception {
        //given
        willDoNothing().given(postService).deleteComment(anyLong(), anyLong(), any());
        //when
        ResultActions response = 댓글_삭제_요청(POST_ID, COMMENT_ID, BEARER + ACCESS_TOKEN);
        //then
        댓글_삭제_성공함(response);
    }

    @DisplayName("게시글 댓글 삭제 - 실패 - 작성자가 아닌 사용자가 삭제 요청 ")
    @Test
    void deleteCommentWhenWrongUser() throws Exception {
        //given
        willThrow(new UnAuthorizedException("댓글 작성자가 아닙니다.")).given(postService).deleteComment(anyLong(), anyLong(), any());
        //when
        ResultActions response = 댓글_삭제_요청(POST_ID, COMMENT_ID, BEARER + "another_user_token");
        //then
        댓글_삭제_실패함(response);
    }

    private ResultActions 댓글_수정_요청(Long postId, Long commentId, CommentRequest request, String accessToken) throws Exception {
        return mockMvc.perform(put("/api/v1/posts/{postId}/comments/{commentId}", postId, commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header(HttpHeaders.AUTHORIZATION, accessToken)
        );
    }

    private void 댓글_수정_성공함(ResultActions response) throws Exception {
        response.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(toDocument("comment-update"));
    }

    private void 댓글_수정_실패함(ResultActions response) throws Exception {
        response.andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(toDocument("comment-update-failure"));
    }

    private ResultActions 댓글_삭제_요청(Long postId, Long commentId, String accessToken) throws Exception {
        return mockMvc.perform(delete("/api/v1/posts/{postId}/comments/{commentId}", postId, commentId)
                .header(HttpHeaders.AUTHORIZATION, accessToken));
    }

    private void 댓글_삭제_성공함(ResultActions response) throws Exception {
        response.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(toDocument("comment-delete"));
    }

    private void 댓글_삭제_실패함(ResultActions response) throws Exception {
        response.andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(toDocument("comment-delete-failure"));
    }

    private ResultActions 글_등록_요청(PostRequest request) throws Exception {
        return mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN));
    }

    private void 글_등록_성공함(ResultActions response, PostResponse postResponse) throws Exception {
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/posts/" + postResponse.getId()))
                .andDo(print())
                .andDo(toDocument("post-create"));
    }

    private void 글_등록_실패함(ResultActions response) throws Exception {
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(toDocument("post-create-failure"));
    }

    private ResultActions 글_단일_조회_요청(Long id) throws Exception {
        return mockMvc.perform(get("/api/v1/posts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN));
    }

    private void 글_단일_조회_성공함(ResultActions response, PostResponse postResponse) throws Exception {
        response.andExpect(status().isOk())
                .andDo(print())
                .andDo(toDocument("post-find"));
    }

    private void 글_단일_조회_실패함(ResultActions response) throws Exception {
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(toDocument("post-find-failure"));
    }

    private ResultActions 글_전체_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN));
    }

    private void 글_전체_조회_성공함(ResultActions response, List<PostResponse> postResponses) throws Exception {
        response.andExpect(status().isOk())
                .andExpect(content().json(toJson(postResponses)))
                .andDo(print())
                .andDo(toDocument("post-findAll"));
    }

    private void 글_전체_조회_성공함_게시글없음(ResultActions response, List<PostResponse> postResponses) throws Exception {
        response.andExpect(status().isOk())
                .andExpect(content().json(toJson(postResponses)))
                .andDo(print())
                .andDo(toDocument("post-findAll-when-empty"));
    }

    private ResultActions 글_수정_요청(Long id, PostRequest request) throws Exception {
        return mockMvc.perform(put("/api/v1/posts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN));
    }

    private void 글_수정_성공함(ResultActions response) throws Exception {
        response.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(toDocument("post-update"));
    }

    private void 글_수정_실패함(ResultActions response) throws Exception {
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(toDocument("post-update-failure"));
    }

    private ResultActions 글_삭제_요청(Long id) throws Exception {
        return mockMvc.perform(delete("/api/v1/posts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN));
    }

    private void 글_삭제_성공함(ResultActions response) throws Exception {
        response.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(toDocument("post-delete"));
    }

    private void 글_삭제_실패함(ResultActions response) throws Exception {
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(toDocument("post-delete-failure"));
    }

    private ResultActions 글_타입별_조회_요청(VaccinationType vaccinationType) throws Exception {
        return mockMvc.perform(get("/api/v1/posts")
                .queryParam("vaccinationType", vaccinationType.name())
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN));
    }

    private void 글_타입별_조회_요청_성공함(ResultActions response) throws Exception {
        response.andExpect(status().isOk())
                .andDo(print())
                .andDo(toDocument("post-findByVaccinationType"));
    }

    private void 글_타입별_조회_성공함_게시글없음(ResultActions response, List<PostResponse> postResponses) throws Exception {
        response.andExpect(status().isOk())
                .andExpect(content().json(toJson(postResponses)))
                .andDo(print())
                .andDo(toDocument("post-findByVaccinationType-when-empty"));
    }

    private ResultActions 글_타입별_페이징_조회_요청(VaccinationType vaccinationType, Long lastPostId, int size) throws Exception {
        return mockMvc.perform(get("/api/v1/posts/paging")
                .queryParam("vaccinationType", vaccinationType.name())
                .queryParam("lastPostId", String.valueOf(lastPostId))
                .queryParam("size", String.valueOf(size))
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN));
    }

    private void 글_타입별_페이징_조회_요청_성공함(ResultActions response) throws Exception {
        response.andExpect(status().isOk())
                .andDo(print())
                .andDo(toDocument("post-findByVaccinationType-paging"));
    }

    private void 글_타입별_페이징_조회_요청_성공함_게시글없음(ResultActions response, List<PostResponse> postResponses) throws Exception {
        response.andExpect(status().isOk())
                .andExpect(content().json(toJson(postResponses)))
                .andDo(print())
                .andDo(toDocument("post-findByVaccinationType-paging-when-empty"));
    }

    private ResultActions 글_좋아요_생성_요청(Long postId) throws Exception {
        return mockMvc.perform(post("/api/v1/posts/{postId}/likes", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN));
    }

    private void 글_좋아요_생성_성공(ResultActions actualResponse, Long likeId) throws Exception {
        actualResponse.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/likes/" + likeId))
                .andDo(print())
                .andDo(toDocument("like-create"));
    }

    private void 글_좋아요_생성_실패(ResultActions response) throws Exception {
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(toDocument("like-create-failure"));
    }

    private ResultActions 글_좋아요_삭제_요청() throws Exception {
        return mockMvc.perform(delete("/api/v1/posts/{postId}/likes", POST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN));
    }

    private void 글_좋아요_삭제_성공함(ResultActions response) throws Exception {
        response.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(toDocument("like-delete"));
    }

    private void 글_좋아요_삭제_실패함(ResultActions response) throws Exception {
        response.andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(toDocument("like-delete-failure"));
    }

    private PostResponse createPostResponse() {
        AgeRangeResponse ageRangeResponse = new AgeRangeResponse(AgeRange.TEENS);
        UserResponse userResponse = new UserResponse(1L, "인비", ageRangeResponse, true,
                ACCESS_TOKEN, SocialProvider.NAVER, "naver_id", "http://naver_url.com");

        return new PostResponse(1L, userResponse, "내용", 1,
                1, true, null, VaccinationType.PFIZER, LocalDateTime.now());
    }

    private ResultActions 댓글_등록_요청(Long postId, CommentRequest request, String headerValue) throws Exception {
        return mockMvc.perform(post("/api/v1/posts/{postId}/comments", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request))
                .header(HttpHeaders.AUTHORIZATION, headerValue));
    }

    private void 댓글_등록_성공함(ResultActions response, CommentResponse expectedResponse) throws Exception {
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/comments/" + expectedResponse.getId()))
                .andDo(print())
                .andDo(toDocument("comment-create"));
    }

    private void 댓글_등록_실패함(ResultActions response) throws Exception {
        response.andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(toDocument("comment-create-failure"));
    }
}
