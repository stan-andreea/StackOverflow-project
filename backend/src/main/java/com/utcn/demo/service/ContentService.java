package com.utcn.demo.service;

import com.utcn.demo.entity.*;
import com.utcn.demo.repository.ContentRepository;
import com.utcn.demo.repository.QuestionTagRepository;
import com.utcn.demo.repository.TagRepository;
import com.utcn.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContentService {

    @Autowired
    ContentRepository contentRepository;
    @Autowired
    QuestionTagRepository questionTagRepository;
    @Autowired
    TagRepository tagRepository;

    public List<Content> retrieveContent() {
        return (List<Content>) contentRepository.findAll();
    }

    public List<Content> retrieveQuestions() {
        List<Content> contentList =  contentRepository.findByQuestionIdIsNull();
        List<Content> sortedContentList = contentList.stream()
                .sorted((c1, c2) -> {
                    int dateComparison = c1.getDateCreated().compareTo(c2.getDateCreated());
                    if (dateComparison != 0) {
                        return dateComparison;
                    } else {
                        return Integer.compare(c1.getTimeMinutes(), c2.getTimeMinutes());
                    }
                })
                .collect(Collectors.toList());
        return sortedContentList;
    }






    public Content retrieveContentById(Long id){
        Optional<Content> content = contentRepository.findById(id);
        if(content.isPresent()){
            return content.get();
        }
        else {
            return null;
        }

    }

    public List<Content> retrieveAnswers(Long id){
         return contentRepository.findByQuestionId(id);

    }

    public void deleteById(Long id) {
        contentRepository.deleteById(id);
    }

    public void deleteQuestion(Long id){
        Optional<Content> question = contentRepository.findById(id);

        // Check if the question exists and is a question (not an answer)
        if (question.isPresent() && question.get().getQuestion() == null) {

            // Find all answers to this question
            List<Content> answers = contentRepository.findByQuestionId(id);

            // Delete all answers
            contentRepository.deleteAll(answers);

            // Delete the question itself
            contentRepository.delete(question.get());
        } else {
            throw new IllegalArgumentException("Message with id " + id + " is not a valid question");
        }
    }

    public Content insertContent(Content content) {
        content.setDateCreated(Date.valueOf(LocalDate.now()));
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        content.setTimeMinutes(hour*60+minute);
        content = contentRepository.save(content);
        for(int i = 0; i < content.getTag_ids().size(); i++){
            System.out.println(content.getTag_ids());
            questionTagRepository.save(new QuestionTag(0L, new Tag(content.getTag_ids().get(i)), new Content(content.getId())));
        }
        return content;
    }

    public Content insertQuestion(Content content, String tagNames) {
        content.setDateCreated(Date.valueOf(LocalDate.now()));
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        content.setTimeMinutes(hour*60+minute);
        content = contentRepository.save(content);

        //add tags
        String[] tagNameArray = tagNames.split(",");
        List<Long> tagIds = new ArrayList<Long>();
        for (String tagName : tagNameArray) {
            Optional<Tag> tagOpt = tagRepository.findByName(tagName.trim());
            if(tagOpt.isPresent() ){
                Tag tag = tagOpt.get();
                if(!tagIds.contains(tag.getId())) {
                    tagIds.add(tag.getId());
                    content.setTag_ids(tagIds);
                    contentRepository.save(content);
                    questionTagRepository.save(new QuestionTag(0L, new Tag(tag.getId()), new Content(content.getId())));
                }
            }
            else {
                Tag tag = new Tag(tagName.trim());
                tagRepository.save(tag);
                tagIds.add(tag.getId());
                content.setTag_ids(tagIds);
                contentRepository.save(content);
                questionTagRepository.save(new QuestionTag(0L, new Tag(tag.getId()), new Content(content.getId())));
            }
        }



        /*for(int i = 0; i < content.getTag_ids().size(); i++){
            System.out.println(content.getTag_ids());

            questionTagRepository.save(new QuestionTag(0L, new Tag(content.getTag_ids().get(i)), new Content(content.getId())));
        }*/
        return content;
    }

    public void addTagsToContent(Long contentId, String tagNames) {
        Content content = retrieveContentById(contentId);
        String[] tagNameArray = tagNames.split(",");
        List<Long> tagIds = content.getTag_ids();
        for (String tagName : tagNameArray) {
            Optional<Tag> tagOpt = tagRepository.findByName(tagName.trim());
            if(tagOpt.isPresent() ){
                Tag tag = tagOpt.get();
                if(!tagIds.contains(tag.getId())) {
                    tagIds.add(tag.getId());
                    content.setTag_ids(tagIds);
                    contentRepository.save(content);
                    questionTagRepository.save(new QuestionTag(0L, new Tag(tag.getId()), new Content(content.getId())));
                }
            }
            else {
                Tag tag = new Tag(tagName.trim());
                tagRepository.save(tag);
                tagIds.add(tag.getId());
                content.setTag_ids(tagIds);
                contentRepository.save(content);
                questionTagRepository.save(new QuestionTag(0L, new Tag(tag.getId()), new Content(content.getId())));
            }
        }
    }



    public Content insertAnswer(Content answer, Long questionId) {
        Optional<Content> optionalQuestion = contentRepository.findById(questionId);
        if (optionalQuestion.isPresent()) {
            Content question = optionalQuestion.get();
            answer.setQuestion(question);
            answer.setDateCreated(Date.valueOf(LocalDate.now()));
            return contentRepository.save(answer);
        } else {
            throw new NoSuchElementException("Question not found");
        }
    }

    public List<Content> getQuestionsByTag(String tag) {
        List<Content> content = (List<Content>)contentRepository.findByQuestionIdIsNull();
        List<Content> filteredContent = new ArrayList<>();

        for(Content c: content){
            for(int i = 0; i < c.getTag_ids().size(); i++){
                Optional<Tag> optionalTag = tagRepository.findById(c.getTag_ids().get(i));
                if(optionalTag.isPresent()){
                    String tagName = optionalTag.get().getName();
                    if(tagName.equals(tag)){
                        filteredContent.add(c);
                    }
                }
            }
        }
        return filteredContent;

    }




    public List<Content> getQuestionsByTitle(String keyword) {
        return contentRepository.findByTitleContainingIgnoreCaseAndQuestionIdIsNull(keyword);
    }

    public List<Content> getQuestionsByUsername(String username) {
        List<Content> questionsByUsername = new ArrayList<>();
        List<Content> questions = (List<Content>) contentRepository.findByQuestionIdIsNull();

        for (Content question : questions) {

            if (question.getAuthor().getUsername().equals(username)) {
                questionsByUsername.add(question);
            }
        }

        return questionsByUsername;
    }

    public List<Tag> getTags(Long questionId) {
        Content content = contentRepository.findById(questionId).orElse(null);
        if (content == null) {
            throw new IllegalArgumentException("Invalid question ID: " + questionId);
        }
        List<Long> tagIds = content.getTag_ids();
        List<Tag> tags = new ArrayList<>();
        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId).orElse(null);
            if (tag != null) {
                tags.add(tag);
            }
        }
        return tags;
    }

    public Content updateConent(Long id, Content updatedContent) {
        Optional<Content> contentOptional = contentRepository.findById(id);

        if (contentOptional.isPresent()) {
            Content content = contentOptional.get();

            content.setTitle(updatedContent.getTitle());
            content.setText(updatedContent.getText());
            content.setImageUrl(updatedContent.getImageUrl());


            return contentRepository.save(content);
        } else {
            return null; // User with the given ID not found
        }
    }



}
