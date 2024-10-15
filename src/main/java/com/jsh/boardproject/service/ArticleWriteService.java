package com.jsh.boardproject.service;

import com.jsh.boardproject.domain.Article;
import com.jsh.boardproject.domain.UserAccount;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
public class ArticleWriteService {
    static Faker faker = new Faker(new Locale("ko","ko"));

    public List<UserAccount> userAccounts(){
        List<UserAccount> userAccounts = new ArrayList<>();

        for (long i = 10; i <= 110; i++) {
            String userId = String.valueOf(i);
            String createdBy = userId;
            String password = faker.internet().password();
            String email = faker.internet().emailAddress()+i;
            String memo = faker.aviation().airline();
            String nickname = faker.name().fullName();
            LocalDateTime createdAt = easyLocaldateTime();
            UserAccount userAccount = new UserAccount(userId,password, email, nickname,memo,createdBy,createdAt);
            userAccounts.add(userAccount);
        }
        System.out.println("----");
        System.out.println(userAccounts.get(0).toString());
        System.out.println("----");
        log.error("[TEST] "+userAccounts.get(0).toString());
        return userAccounts;
    }

    public List<Article> articles(List<UserAccount> userAccounts,long startIdx, long endIdx){
        List<Article> articles = new ArrayList<>();
        for (long i = startIdx; i < endIdx; i++) {
            int pickUser = faker.number().numberBetween(0,100);
            UserAccount userAccount = userAccounts.get(pickUser);

            Long id = i;
            LocalDateTime createdAt = easyLocaldateTime();
            String content = faker.text().text(100);
            String title = faker.text().text(50);

            Article article = new Article(id,userAccount, title, content, createdAt);
            articles.add(article);
        }
        return articles;
    }

    public List<Article> dummyArticles(){
        List<UserAccount> userAccounts = userAccounts();
        return articles(userAccounts,0,1000000);
    }

    LocalDateTime easyLocaldateTime(){
        int h = faker.number().numberBetween(0,23);
        int m = faker.number().numberBetween(0,59);
        int s = faker.number().numberBetween(0,59);
        return faker.date().birthdayLocalDate().atTime(LocalTime.of(h,m,s));
    }
}
