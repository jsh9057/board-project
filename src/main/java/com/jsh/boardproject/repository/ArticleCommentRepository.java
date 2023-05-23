package com.jsh.boardproject.repository;

import com.jsh.boardproject.domain.ArticleComment;
import com.jsh.boardproject.domain.QArticle;
import com.jsh.boardproject.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleCommentRepository
        extends JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {  // 추가 기능을 만들기위해 커스터마이즈를 구현을 사용
        bindings.excludeUnlistedProperties(true); // 현재 QuerydslPredicateExecutor 때문에 Article 에 대한 모든 검색이 열려있음.
        // 하지만 부분적인 검색을 원하기 때문에 필요없는건 검색에서 제외하려함
        bindings.including(root.content, root.createdAt, root.createdBy); // 검색에 사용할 요소를 추가
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase);  // like '${v}'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
