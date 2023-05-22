package com.utcn.demo.controller;

import com.utcn.demo.entity.Content;
import com.utcn.demo.entity.User;
import com.utcn.demo.service.UserService;
import com.utcn.demo.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class VoteController {
    @Autowired
    VoteService voteService;

    @PostMapping("/vote/like/{userId}/{contentId}")
    public String likeQuestion(@PathVariable Long userId, @PathVariable Long contentId) {
        if(voteService.likeQuestion(userId, contentId)!=null)
         return "Question liked successfully.";
        else return "Error while liking question";
    }

    @PostMapping("/vote/dislike/{userId}/{contentId}")
    public String dislikeQuestion(@PathVariable Long userId, @PathVariable Long contentId) {
        if(voteService.dislikeQuestion(userId, contentId)!=null)
            return "Question disliked successfully.";
        else return "Error while liking question";
    }

    @DeleteMapping("/vote/delete/{userId}/{contentId}")
    public void deleteVote(@PathVariable Long userId, @PathVariable Long contentId) {
        voteService.removeVote(userId, contentId);
    }

    @GetMapping("/vote/count/{contentId}")
    public int computeVoteCount(@PathVariable Long contentId) {
        int voteCount = voteService.computeVoteCount(contentId);
        return voteCount;
    }

    @GetMapping("/vote/hasVoted/{userId}/{contentId}")
    public boolean hasVoted(@PathVariable Long userId, @PathVariable Long contentId) {
        return voteService.hasVoted(userId, contentId);
    }

}
