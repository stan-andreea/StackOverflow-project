package com.utcn.demo.repository;

import com.utcn.demo.entity.Content;
import com.utcn.demo.entity.Tag;
import jakarta.persistence.OrderBy;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends CrudRepository<Content, Long> {
    List<Content> findByQuestionIdIsNull();


    List<Content> findByQuestionId(Long questionId);


    List<Content> findByTitleContainingIgnoreCase(String keyword);

    List<Content> findByTitleContainingIgnoreCaseAndQuestionIdIsNull(String keyword);
}
