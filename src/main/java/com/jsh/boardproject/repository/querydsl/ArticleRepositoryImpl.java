package com.jsh.boardproject.repository.querydsl;

import com.jsh.boardproject.domain.Article;
import com.jsh.boardproject.domain.QArticle;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryImpl() {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;

        return from(article)
                .distinct()
                .select(article.hashtag)
                .where(article.hashtag.isNotNull())
                .fetch();
    }
}
