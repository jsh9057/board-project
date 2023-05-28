package com.jsh.boardproject.service;

import static org.assertj.core.api.Assertions.*;

import com.jsh.boardproject.domain.Article;
import com.jsh.boardproject.domain.ArticleComment;
import com.jsh.boardproject.domain.UserAccount;
import com.jsh.boardproject.dto.ArticleCommentDto;
import com.jsh.boardproject.dto.UserAccountDto;
import com.jsh.boardproject.repository.ArticleCommentRepository;
import com.jsh.boardproject.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService sut;

    @Mock private ArticleRepository articleRepository;
    @Mock private ArticleCommentRepository articleCommentRepository;

    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {
        // Given
        Long articleId = 1L;
        ArticleComment expected = createArticleComment("content");
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));

        // When
        List<ArticleCommentDto> actual = sut.searchArticleComments(articleId);

        // Then
        assertThat(actual)
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("content",expected.getContent());
        then(articleCommentRepository).should().findByArticle_Id(articleId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        // When
        sut.saveArticleComment(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    private ArticleComment createArticleComment(String content){
        return ArticleComment.of(
                Article.of(createUserAccount(), "title", "content", "hashtag"),
                createUserAccount(),
                content
        );
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.of(
                1L,
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

    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createUserAccountDto(),
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
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }
}