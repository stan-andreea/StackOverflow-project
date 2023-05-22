package com.utcn.demo.repository;

import com.utcn.demo.entity.Content;
import com.utcn.demo.entity.QuestionTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTagRepository extends CrudRepository<QuestionTag, Long> {
}
