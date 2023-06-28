package com.jsh.boardproject.repository;

import com.jsh.boardproject.domain.Article;
import com.jsh.boardproject.domain.QArticle;
import com.jsh.boardproject.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        QuerydslPredicateExecutor<Article>,     // 엔티티 안에있는 모든 필드에 대한 기본 검색기능을 추가해 줌
        QuerydslBinderCustomizer<QArticle> {

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);

    void deleteByIdAndUserAccount_UserId(Long articleId, String UserId);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {  // 추가 기능을 만들기위해 커스터마이즈를 구현을 사용
        bindings.excludeUnlistedProperties(true); // 현재 QuerydslPredicateExecutor 때문에 Article 에 대한 모든 검색이 열려있음.
                                                  // 하지만 부분적인 검색을 원하기 때문에 필요없는건 검색에서 제외하려함
        bindings.including(root.title, root.content ,root.hashtag, root.createdAt, root.createdBy); // 검색에 사용할 요소를 추가
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%${v}%' , 대소문자 무시
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);    // date 라서
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
