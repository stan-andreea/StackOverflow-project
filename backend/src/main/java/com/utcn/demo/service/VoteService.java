package com.utcn.demo.service;

import com.utcn.demo.entity.*;
import com.utcn.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private UserRepository userRepository;


    public String likeQuestion(Long userId, Long contentId) {
        double scoreIncrement;
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Content> contentOptional = contentRepository.findById(contentId);
        if(userOptional.isPresent() && contentOptional.isPresent()){
            User user = userOptional.get();
            Content content = contentOptional.get();
            User author = content.getAuthor();
            if (user.getUserId().equals(content.getAuthor().getUserId())) {
                return null;
            }
            Vote vote = voteRepository.findByUserAndContent(user, content);

            if (vote == null) {
                if(content.getQuestion()==null)
                      scoreIncrement = 2.5; // +2.5 points for a question upvote
                else
                    scoreIncrement = 5;
                author.setPoints(author.getPoints()+scoreIncrement);


                // Save the updated user score
                userRepository.save(author);

                vote = new Vote();
                vote.setUser(user);
                vote.setContent(content);
                vote.setLiked(1);
                voteRepository.save(vote);
                return "Vote added";
            }
            else return null;

        }
        else {
            return null;
        }
    }

    public Vote dislikeQuestion(Long userId, Long contentId) {
        double scoreDecrementAuthor = 0;
        double scoreDecrementCurrentUser = 0;

        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Content> contentOptional = contentRepository.findById(contentId);
        if(userOptional.isPresent() && contentOptional.isPresent()){
            User user = userOptional.get();
            Content content = contentOptional.get();
            User author = content.getAuthor();

            if (user.getUserId().equals(content.getAuthor().getUserId())) {
                return null;
            }
            System.out.println("entered");
            Vote vote = voteRepository.findByUserAndContent(user, content);
            if (vote == null) {
                if(content.getQuestion()==null)
                    scoreDecrementAuthor = 1.5; // +2.5 points for a question upvote
                else {
                    scoreDecrementAuthor = 2.5;
                    scoreDecrementCurrentUser = 1.5;
                }
                author.setPoints(author.getPoints()- scoreDecrementAuthor);
                userRepository.save(author);
                user.setPoints(user.getPoints() - scoreDecrementCurrentUser);

                vote = new Vote();
                vote.setUser(user);
                vote.setContent(content);
                vote.setLiked(0);
                voteRepository.save(vote);
                return vote;
            }
            else return null;
        }

        return null;

    }

    public boolean hasVoted(Long userId, Long contentId) {

        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Content> contentOptional = contentRepository.findById(contentId);
        if(userOptional.isPresent() && contentOptional.isPresent()){
            User user = userOptional.get();
            Content content = contentOptional.get();
            User author = content.getAuthor();

            if (user.getUserId().equals(content.getAuthor().getUserId())) {
                return false;
            }

            Vote vote = voteRepository.findByUserAndContent(user, content);
            if (vote == null) {
                return false;
            }
            else return true;
        }
        return false;


    }


    public void removeVote(Long userId, Long contentId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Content> contentOptional = contentRepository.findById(contentId);
        if(userOptional.isPresent() && contentOptional.isPresent()) {
            User user = userOptional.get();
            Content content = contentOptional.get();
            Vote vote = voteRepository.findByUserAndContent(user, content);
            if (vote != null) {
                voteRepository.delete(vote);
            }
        }
    }

    public int computeVoteCount(Long contentId) {
        Optional<Content> contentOptional = contentRepository.findById(contentId);
        if(contentOptional.isPresent()){
            int likes = voteRepository.countByContentAndLiked(contentOptional.get(), 1);
            System.out.println("likes" + likes);
            int dislikes = voteRepository.countByContentAndLiked(contentOptional.get(), 0);
            System.out.println("dislikes" + dislikes);
            return likes - dislikes;
        }
        return 0;

    }

}
