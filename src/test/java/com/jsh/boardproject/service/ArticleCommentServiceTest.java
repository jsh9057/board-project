package com.jsh.boardproject.service;

import static org.assertj.core.api.Assertions.*;

import com.jsh.boardproject.domain.Article;
import com.jsh.boardproject.domain.ArticleComment;
import com.jsh.boardproject.domain.Hashtag;
import com.jsh.boardproject.domain.UserAccount;
import com.jsh.boardproject.dto.ArticleCommentDto;
import com.jsh.boardproject.dto.UserAccountDto;
import com.jsh.boardproject.repository.ArticleCommentRepository;
import com.jsh.boardproject.repository.ArticleRepository;
import com.jsh.boardproject.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService sut;

    @Mock private ArticleRepository articleRepository;
    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private UserAccountRepository userAccountRepository;


    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {
        // Given
        Long articleId = 1L;
        ArticleComment expectedParentComment = createArticleComment(1L,"parent content");
        ArticleComment expectedChildComment = createArticleComment(2L,"child content");
        expectedChildComment.setParentCommentId(expectedParentComment.getId());
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(
                expectedParentComment,
                expectedChildComment
        ));

        // When
        List<ArticleCommentDto> actual = sut.searchArticleComments(articleId);

        // Then
        assertThat(actual).hasSize(2);
        assertThat(actual)
                .extracting("id","articleId","parentCommentId","content")
                .containsExactlyInAnyOrder(
                        tuple(1L,1L,null,"parent content"),
                        tuple(2L,1L,1L,"child content")
                );
        then(articleCommentRepository).should().findByArticle_Id(articleId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(createUserAccount());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        // When
        sut.saveArticleComment(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안 한다.")
    @Test
    void givenNonexistentArticle_whenSavingArticle_thenLogsSituationAndDoseNothing() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);

        // When
        sut.saveArticleComment(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(userAccountRepository).shouldHaveNoInteractions();
        then(articleCommentRepository).should(never()).getReferenceById(anyLong());
        then(articleCommentRepository).shouldHaveNoInteractions();
    }

    @DisplayName("부모 댓글 ID와 댓글 정보를 입력하면, 대댓글을 저장한다.")
    @Test
    void givenParentCommentIdAndArticleCommentInfo_whenSaving_thenSavesChildComment() {
        // Given
        Long parentCommentId = 1L;
        ArticleComment parent = createArticleComment(parentCommentId,"댓글");
        ArticleCommentDto child = createArticleCommentDto(parentCommentId,"대댓글");
        given(articleRepository.getReferenceById(child.articleId())).willReturn(createArticle());
        given(userAccountRepository.getReferenceById(child.userAccountDto().userId())).willReturn(createUserAccount());
        given(articleCommentRepository.getReferenceById(child.parentCommentId())).willReturn(parent);

        // When
        sut.saveArticleComment(child);

        // Then
        assertThat(child.parentCommentId()).isNotNull();
        then(articleCommentRepository).should().getReferenceById(child.articleId());
        then(userAccountRepository).should().getReferenceById(child.userAccountDto().userId());
        then(articleCommentRepository).should().getReferenceById(child.parentCommentId());
        then(articleCommentRepository).should(never()).save(any(ArticleComment.class));

    }

    @DisplayName("없은 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무 것도 안 한다.")
    @Test
    void givenNonexistentArticleCommentInfo_whenUpdatingArticleComment_thenLogsWarningAndDoseNothing() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticleComment(dto);

        // Then
        then(articleCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment() {
        // Given
        Long articleCommentId = 1L;
        String userId = "jsh";
        willDoNothing().given(articleCommentRepository).deleteByIdAndUserAccount_UserId(articleCommentId,userId);

        // When
        sut.deleteArticleComment(articleCommentId,userId);

        // Then
        then(articleCommentRepository).should().deleteByIdAndUserAccount_UserId(articleCommentId,userId);
    }

    @DisplayName("없는 댓글을 삭제하려고하면, 경고 로그를 찍고 아무것도 안 한다.")
    @Test
    void givenNonexistentArticleComment_whenDeletingArticleComment_thenLogsWarningAndDoseNothing() {
        // Given
        Long articleCommentId = 1L;
        String userId = "jsh";
        willDoNothing().given(articleCommentRepository).deleteByIdAndUserAccount_UserId(articleCommentId,userId);

        // When
        sut.deleteArticleComment(articleCommentId,userId);

        // Then
        then(articleCommentRepository).should().deleteByIdAndUserAccount_UserId(articleCommentId,userId);
    }

    private ArticleCommentDto createArticleCommentDto(String content){
        return createArticleCommentDto(null,content);
    }

    private ArticleComment createArticleComment(Long id, String content){
        ArticleComment articleComment = ArticleComment.of(
                createArticle(),
                createUserAccount(),
                content
        );
        ReflectionTestUtils.setField(articleComment,"id",id);

        return articleComment;
    }

    private ArticleCommentDto createArticleCommentDto(Long parentCommentId, String content){
        return createArticleCommentDto(1L,parentCommentId,content);
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.of(
                "jsh",
                "password",
                "jsh@mail.com",
                "Jsh",
                "This is memo",
                LocalDateTime.now(),
                "jsh",
                LocalDateTime.now(),
                "jsh"
        );
    }

    private ArticleCommentDto createArticleCommentDto(Long id, Long parentCommentId, String content) {
        return ArticleCommentDto.of(
                id,
                1L,
                createUserAccountDto(),
                parentCommentId,
                content,
                LocalDateTime.now(),
                "jsh",
                LocalDateTime.now(),
                "jsh"

        );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "jsh",
                "password",
                "jsh@mail.com",
                "Jsh",
                null
        );
    }

    private Article createArticle(){
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content"
        );
        ReflectionTestUtils.setField(article,"id",1L);
        article.addHashtags(Set.of(createHashtag(article)));

        return article;
    }

    private Hashtag createHashtag(Article article){
        return Hashtag.of("java");
    }
}