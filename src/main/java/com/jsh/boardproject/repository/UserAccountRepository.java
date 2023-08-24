package com.jsh.boardproject.repository;

import com.jsh.boardproject.domain.UserAccount;
import com.jsh.boardproject.domain.projection.ArticleProjection;
import com.jsh.boardproject.domain.projection.UserAccountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = UserAccountProjection.class)
public interface UserAccountRepository extends JpaRepository<UserAccount,String> {
}
