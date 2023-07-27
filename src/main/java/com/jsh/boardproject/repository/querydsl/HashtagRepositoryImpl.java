package com.jsh.boardproject.repository.querydsl;

import com.jsh.boardproject.domain.Hashtag;
import com.jsh.boardproject.domain.QHashtag;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class HashtagRepositoryImpl extends QuerydslRepositorySupport implements HashtagRepositoryCustom {
    public HashtagRepositoryImpl(){
        super(Hashtag.class);
    }

    @Override
    public List<String> findAllHashtagNames() {
        QHashtag hashtag = QHashtag.hashtag;

        return from(hashtag)
                .select(hashtag.hashtagName)
                .fetch();

    }
}
