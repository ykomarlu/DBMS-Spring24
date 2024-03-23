/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.Post;
import uga.menik.cs4370.models.User;
import uga.menik.cs4370.services.PostService;
import uga.menik.cs4370.services.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final PostService postService;
    @Autowired
    public ProfileController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public ModelAndView profileOfLoggedInUser() {
        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser == null) {
            // Handle the case when no user is logged in
            return new ModelAndView("redirect:/login");
        }

        String userId = loggedInUser.getUserId();
        List<Post> posts = postService.getPostsByUserIdOrderByDateDesc(userId);

        ModelAndView mv = new ModelAndView("posts_page");
        mv.addObject("user", loggedInUser);
        mv.addObject("posts", posts);

        if (posts.isEmpty()) {
            mv.addObject("isNoContent", true);
        }

        return mv;
    }

    @GetMapping("/{userId}")
    public ModelAndView profileOfSpecificUser(@PathVariable("userId") String userId) {
        try {
            User user = userService.getUserById(userId);
            List<Post> posts = postService.getPostsByUserIdOrderByDateDesc(userId);

            ModelAndView mv = new ModelAndView("posts_page");
            mv.addObject("user", user);
            mv.addObject("posts", posts);

            if (posts.isEmpty()) {
                mv.addObject("isNoContent", true);
            }

            return mv;
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("error");
            mv.addObject("errorMessage", "Failed to load user profile.");
            return mv;
        }
    }
}
