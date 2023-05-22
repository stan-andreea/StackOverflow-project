package com.utcn.demo;

import com.utcn.demo.controller.ContentController;
import com.utcn.demo.controller.UserController;
import com.utcn.demo.entity.Content;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest
public class ContentTests {

    @Autowired
    private ContentController contentController;

    @Autowired
    private UserController userController;


    // JUnit test for saveEmployee
    @Test
    @Order(1)
    @Rollback(value = false)
    public void saveContentTest(){

       User user = userController.getById(1L);


        Content content = new Content();
        content.setAuthor(user);
        content.setQuestion(null);
        content.setTitle("Sample Content");
        content.setText("sample");
        content.setImageUrl("test");
        Content newContent = contentController.insertQuestion(content,"tag1,tag2");

        Assertions.assertThat(!Objects.isNull(newContent));
    }

    @Test
    @Order(2)
    public void getContentTest(){
        Content content = contentController.getById(52L);
        Assertions.assertThat(content.getId()).isEqualTo(52L);
    }

    @Test
    @Order(3)
    public void getAllContentsTest(){
        List<Content> contents = contentController.getQuestionsByUsername("abc");
        Assertions.assertThat(contents.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updateContentTest(){
        Content content = contentController.getById(52L);
        content.setTitle("Updated Content");

        Content updatedContent = contentController.updateContent(52L,content);

        Assertions.assertThat(updatedContent.getTitle()).isEqualTo("Updated Content");
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    public void deleteContentTest(){
        Content content = contentController.getById(806L);
        contentController.deleteById(806L);
        Optional<Content> optionalContent = Optional.ofNullable(contentController.getById(806L));

        Assertions.assertThat(Objects.isNull(optionalContent));
    }
}


