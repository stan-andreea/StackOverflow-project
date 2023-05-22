package com.utcn.demo.repository;

import com.utcn.demo.entity.Content;
import com.utcn.demo.entity.User;
import com.utcn.demo.entity.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Long> {
    Vote findByUserAndContent(User user, Content content);

    int countByContentAndLiked(Content content, int i);
}
