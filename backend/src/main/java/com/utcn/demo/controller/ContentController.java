package com.utcn.demo.controller;

import com.utcn.demo.entity.Content;
import com.utcn.demo.entity.Tag;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.TagRepository;
import com.utcn.demo.service.ContentService;
import com.utcn.demo.service.UserService;
import com.utcn.demo.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class ContentController {

    @Autowired
    ContentService contentService;

    @Autowired
    VoteService voteService;

    @GetMapping( "/content/getAll")
    @ResponseBody
    public List<Content> retrieveContent() {
        return contentService.retrieveContent();
    }

    @GetMapping( "/content/getQuestions")
    @ResponseBody
    public List<Content> retrieveQuestions() {
        return contentService.retrieveQuestions();
    }

    @GetMapping("content/getById/{id}")
    @ResponseBody
    public Content getById(@PathVariable Long id) {
        return  contentService.retrieveContentById(id);
    }

    @GetMapping("content/getAnswers/{id}")
    @ResponseBody
    public List<Content> getAnswers(@PathVariable Long id) {
        List<Content> answers =   contentService.retrieveAnswers(id);
        List<Content> sortedAnswers = answers.stream()
                    .sorted((a1, a2) -> {
                        int voteCountComparison = Integer.compare(
                                voteService.computeVoteCount(a2.getId()), voteService.computeVoteCount(a1.getId()));
                        if (voteCountComparison != 0) {
                            return voteCountComparison;
                        } else {
                            int dateComparison = a1.getDateCreated().compareTo(a2.getDateCreated());
                            if (dateComparison != 0) {
                                return dateComparison;
                            } else {
                                return Integer.compare(a1.getTimeMinutes(), a2.getTimeMinutes());
                            }
                        }
                    })
                    .collect(Collectors.toList());

            return sortedAnswers;
    }



    @DeleteMapping("content/deleteById/{id}")
    public void deleteById(@PathVariable Long id) {contentService.deleteById(id);}


    @DeleteMapping("content/deleteQuestion/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        contentService.deleteQuestion(id);
    }

    @PostMapping("/content/insertQuestion")
    public Content insertContent(@RequestBody Content content) {
        return contentService.insertContent(content);
    }

    @PostMapping("/content/insertQuestionWithTags/{tagNames}")
    public Content insertQuestion(@RequestBody Content content, @PathVariable String tagNames) {
        return contentService.insertQuestion(content,tagNames);
    }

    @PostMapping("/content/addTags/{contentId}/tag/{tagNames}")
    public void addTagsToContent(@PathVariable Long contentId, @PathVariable String tagNames) {

        contentService.addTagsToContent(contentId, tagNames);
    }

    @PostMapping("/content/insertAnswer/{id}")
    public Content insertAnswer(@RequestBody Content content, @PathVariable Long id) {
        return contentService.insertAnswer(content,id);
    }

    @GetMapping("/content/searchByTag/{tag}")
    public List<Content> getQuestionsByTag(@PathVariable("tag") String tagName) {
        return contentService.getQuestionsByTag(tagName);
    }

    @GetMapping("/content/searchByKeyword/{keyword}")
    public List<Content> getQuestionsByKeyword(@PathVariable("keyword") String keyword) {
        return contentService.getQuestionsByTitle(keyword);
    }

    @GetMapping("/content/searchByUsername/{username}")
    public List<Content> getQuestionsByUsername(@PathVariable("username") String username) {
        return contentService.getQuestionsByUsername(username);
    }


    @GetMapping("/content/getTags/{questionId}")
    public List<Tag> getTags(@PathVariable Long questionId) {
        List<Tag> tags = contentService.getTags(questionId);
        return tags;
    }


    @PutMapping("/content/update/{id}")
    public Content updateContent(@PathVariable Long id, @RequestBody Content updatedContent) {
        Content content = contentService.updateConent(id, updatedContent);

        if (content != null) {
            return content;
        } else {
            return null;
        }
    }




}



