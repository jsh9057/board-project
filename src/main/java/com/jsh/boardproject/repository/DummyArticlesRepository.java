package com.jsh.boardproject.repository;

import com.jsh.boardproject.domain.Article;
import com.jsh.boardproject.domain.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DummyArticlesRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAllArticle(List<Article> articles) {
//        List<List<Article>> listList = new ArrayList<>();
//        int start = 0;
//        int end = 10000;
//        for (int i = 0; i < 100; i++) {
//            listList.add(new ArrayList<>(articles.subList(start,end)));
//            start = end+1;
//            end = end+10000;
//        }
//
//        for (int i = 0; i < 100; i++) {
//            List<Article> blockArticles = listList.get(i);
//            String sql = "INSERT INTO article (id, title, content, user_id, created_at, created_by, modified_at, modified_by) " +
//                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//
//            jdbcTemplate.batchUpdate(sql,
//                    blockArticles,
//                    blockArticles.size(),
//                    (PreparedStatement ps, Article article) -> {
//                        ps.setLong(1, article.getId());
//                        ps.setString(2, article.getTitle());
//                        ps.setString(3, article.getContent());
//                        ps.setString(4, article.getUserAccount().getUserId());
//                        ps.setTimestamp(5, Timestamp.valueOf(article.getCreatedAt()));
//                        ps.setString(6, article.getUserAccount().getUserId());
//                        ps.setTimestamp(7, Timestamp.valueOf(article.getCreatedAt()));
//                        ps.setString(8, article.getUserAccount().getUserId());
//                    });
//        }
        String sql = "INSERT INTO article (id, title, content, user_id, created_at, created_by, modified_at, modified_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        ArrayList<Article> articles1 = new ArrayList<>(articles.subList(0,200000));
        System.out.println(articles1.get(0).getId());
        System.out.println(articles1.get(199999).getId());
        ArrayList<Article> articles2 = new ArrayList<>(articles.subList(200000,400000));
        System.out.println(articles2.get(0).getId());
        ArrayList<Article> articles3 = new ArrayList<>(articles.subList(400001,600000));
        ArrayList<Article> articles4 = new ArrayList<>(articles.subList(600001,800000));
        ArrayList<Article> articles5 = new ArrayList<>(articles.subList(800001,1000000));
        jdbcTemplate.batchUpdate(sql,
                articles1,
                articles1.size(),
                (PreparedStatement ps, Article article) -> {
                    ps.setLong(1, article.getId());
                    ps.setString(2, article.getTitle());
                    ps.setString(3, article.getContent());
                    ps.setString(4, article.getUserAccount().getUserId());
                    ps.setTimestamp(5, Timestamp.valueOf(article.getCreatedAt()));
                    ps.setString(6, article.getUserAccount().getUserId());
                    ps.setTimestamp(7, Timestamp.valueOf(article.getCreatedAt()));
                    ps.setString(8, article.getUserAccount().getUserId());
                });
        jdbcTemplate.batchUpdate(sql,
                articles2,
                articles2.size(),
                (PreparedStatement ps, Article article) -> {
                    ps.setLong(1, article.getId());
                    ps.setString(2, article.getTitle());
                    ps.setString(3, article.getContent());
                    ps.setString(4, article.getUserAccount().getUserId());
                    ps.setTimestamp(5, Timestamp.valueOf(article.getCreatedAt()));
                    ps.setString(6, article.getUserAccount().getUserId());
                    ps.setTimestamp(7, Timestamp.valueOf(article.getCreatedAt()));
                    ps.setString(8, article.getUserAccount().getUserId());
                });
        jdbcTemplate.batchUpdate(sql,
                articles3,
                articles3.size(),
                (PreparedStatement ps, Article article) -> {
                    ps.setLong(1, article.getId());
                    ps.setString(2, article.getTitle());
                    ps.setString(3, article.getContent());
                    ps.setString(4, article.getUserAccount().getUserId());
                    ps.setTimestamp(5, Timestamp.valueOf(article.getCreatedAt()));
                    ps.setString(6, article.getUserAccount().getUserId());
                    ps.setTimestamp(7, Timestamp.valueOf(article.getCreatedAt()));
                    ps.setString(8, article.getUserAccount().getUserId());
                });
        jdbcTemplate.batchUpdate(sql,
                articles4,
                articles4.size(),
                (PreparedStatement ps, Article article) -> {
                    ps.setLong(1, article.getId());
                    ps.setString(2, article.getTitle());
                    ps.setString(3, article.getContent());
                    ps.setString(4, article.getUserAccount().getUserId());
                    ps.setTimestamp(5, Timestamp.valueOf(article.getCreatedAt()));
                    ps.setString(6, article.getUserAccount().getUserId());
                    ps.setTimestamp(7, Timestamp.valueOf(article.getCreatedAt()));
                    ps.setString(8, article.getUserAccount().getUserId());
                });
        jdbcTemplate.batchUpdate(sql,
                articles5,
                articles5.size(),
                (PreparedStatement ps, Article article) -> {
                    ps.setLong(1, article.getId());
                    ps.setString(2, article.getTitle());
                    ps.setString(3, article.getContent());
                    ps.setString(4, article.getUserAccount().getUserId());
                    ps.setTimestamp(5, Timestamp.valueOf(article.getCreatedAt()));
                    ps.setString(6, article.getUserAccount().getUserId());
                    ps.setTimestamp(7, Timestamp.valueOf(article.getCreatedAt()));
                    ps.setString(8, article.getUserAccount().getUserId());
                });

    }

    @Transactional
    public void saveAllUser(List<UserAccount> userAccounts){
        String userSql = "INSERT INTO user_account (user_id, created_at, created_by, modified_at, modified_by, email, memo, nickname, user_password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(userSql,
                userAccounts,
                userAccounts.size(),
                (PreparedStatement ps, UserAccount userAccount) -> {
                    ps.setString(1,userAccount.getUserId());
                    ps.setTimestamp(2,Timestamp.valueOf(userAccount.getCreatedAt()));
                    ps.setString(3,userAccount.getUserId());
                    ps.setTimestamp(4,Timestamp.valueOf(userAccount.getCreatedAt()));
                    ps.setString(5,userAccount.getUserId());
                    ps.setString(6,userAccount.getEmail());
                    ps.setString(7,userAccount.getMemo());
                    ps.setString(8,userAccount.getNickname());
                    ps.setString(9,userAccount.getUserPassword());
                });

    }
}
