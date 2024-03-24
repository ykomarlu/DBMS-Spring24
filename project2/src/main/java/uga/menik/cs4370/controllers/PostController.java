/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.ExpandedPost;
import uga.menik.cs4370.services.PostService;

/**
 * Handles /post URL and its sub urls.
 */
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * This function handles the /post/{postId} URL.
     * This handlers serves the web page for a specific post.
     * Note there is a path variable {postId}.
     * An example URL handled by this function looks like below:
     * http://localhost:8081/post/1
     * The above URL assigns 1 to postId.
     * 
     * See notes from HomeController.java regardig error URL parameter.
     */
    @GetMapping("/{postId}")
    public ModelAndView webpage(@PathVariable("postId") String postId, @RequestParam(name = "error", required = false) String error) {
        ModelAndView mv = new ModelAndView("posts_page");

        try {
            ExpandedPost post = postService.getPostById(Integer.parseInt(postId));
            if (post != null) {
                mv.addObject("posts", post);
            } else {
                mv.addObject("isNoContent", true);
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            mv.addObject("errorMessage", errorMessage);
        }

        return mv;
    }

    /**
     * Handles comments added on posts.
     * See comments on webpage function to see how path variables work here.
     * This function handles form posts.
     * See comments in HomeController.java regarding form submissions.
     */
    @PostMapping("/{postId}/comment")
    public String postComment(@PathVariable("postId") String postId, @RequestParam(name = "comment") String comment) {

        try {
            postService.newComment(Integer.parseInt(postId), comment);

            return "redirect:/post/" + postId;
        } catch (Exception e) {
            String message = URLEncoder.encode("Failed to post the comment. Please try again.", StandardCharsets.UTF_8);
            return "redirect:/post/" + postId + "?error=" + message;
        }
    }

    /**
     * Handles likes added on posts.
     * See comments on webpage function to see how path variables work here.
     * See comments in PeopleController.java in followUnfollowUser function regarding 
     * get type form submissions and how path variables work.
     */
    @GetMapping("/{postId}/heart/{isAdd}")
    public String addOrRemoveHeart(@PathVariable("postId") String postId, @PathVariable("isAdd") Boolean isAdd) {
        try {
            postService.likePost(Integer.parseInt(postId), isAdd);
            return "redirect:/post/" + postId;
        }
        catch(SQLException s) {
            String message = URLEncoder.encode("Failed to (un)like the post. Please try again.",
                    StandardCharsets.UTF_8);
            return "redirect:/post/" + postId + "?error=" + message;
        }
    }

    /**
     * Handles bookmarking posts.
     * See comments on webpage function to see how path variables work here.
     * See comments in PeopleController.java in followUnfollowUser function regarding 
     * get type form submissions.
     */
    @GetMapping("/{postId}/bookmark/{isAdd}")
    public String addOrRemoveBookmark(@PathVariable("postId") String postId, @PathVariable("isAdd") Boolean isAdd) {
        try {
            postService.modifyBookmark(Integer.parseInt(postId), isAdd);
            return "redirect:/post/" + postId;
        }
        catch (SQLException s) {
            String message = URLEncoder.encode("Failed to (un)bookmark the post. Please try again.",
                    StandardCharsets.UTF_8);
            return "redirect:/post/" + postId + "?error=" + message;
        }
    }

}
