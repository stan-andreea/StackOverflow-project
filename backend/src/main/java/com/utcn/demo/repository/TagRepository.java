package com.utcn.demo.repository;

import com.utcn.demo.entity.Tag;
import com.utcn.demo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
    Optional<Tag> findByName(String tagName);


}
