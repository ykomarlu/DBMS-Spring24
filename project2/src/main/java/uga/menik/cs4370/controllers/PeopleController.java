/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.FollowableUser;
import uga.menik.cs4370.services.PeopleService;
import uga.menik.cs4370.services.UserService;

/**
 * Handles /people URL and its sub URL paths.
 */
@Controller
@RequestMapping("/people")
public class PeopleController {

    private final UserService userService;
    private final PeopleService peopleService;
    private final DataSource dataSource;

    /**
     * See notes in AuthInterceptor.java regarding how this works 
     * through dependency injection and inversion of control.
     */
    @Autowired
    public PeopleController(UserService userService, PeopleService peopleService, DataSource dataSource) {
        this.userService = userService;
        this.peopleService = peopleService;
        this.dataSource = dataSource;
    }

    /**
     * Serves the /people web page.
     * 
     * Note that this accepts a URL parameter called error.
     * The value to this parameter can be shown to the user as an error message.
     * See notes in HashtagSearchController.java regarding URL parameters.
     */
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("people_page");

        try {
            List<FollowableUser> followableUsers = peopleService.getFollowableUsers(userService.getLoggedInUser().getUserId());

            if (followableUsers.size() == 0) {
                mv.addObject("isNoContent", true);
            } else {
                mv.addObject("users", followableUsers);
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            mv.addObject("errorMessage", errorMessage);
        }

        return mv;
    }

    /**
     * This function handles user follow and unfollow.
     * Note the URL has parameters defined as variables ie: {userId} and {isFollow}.
     * Follow and unfollow is handled by submitting a get type form to this URL 
     * by specifing the userId and the isFollow variables.
     * Learn more here: https://www.w3schools.com/tags/att_form_method.asp
     * An example URL that is handled by this function looks like below:
     * http://localhost:8081/people/1/follow/false
     * The above URL assigns 1 to userId and false to isFollow.
     */
    @GetMapping("{userId}/follow/{isFollow}")
    public String followUnfollowUser(@PathVariable("userId") String userId,
            @PathVariable("isFollow") Boolean isFollow) throws SQLException{
        
        System.out.println("User is attempting to follow/unfollow a user:");
        System.out.println("\tuserId: " + userId);
        System.out.println("\tisFollow: " + isFollow);
        
        if(isFollow == true) {
            String sql = "insert into follow values (?,?)";
            try (Connection conn = dataSource.getConnection(); 
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, userService.getLoggedInUser().getUserId());
                pstmt.setString(2, userId);
                int rowsAffected = pstmt.executeUpdate();
                if(rowsAffected > 0)
                    return "redirect:/people";
            }
            
        }
        else{
            String sql = "delete from follow where followeeUserId = ? and followerUserId = ?;";
            try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
                    pstmt.setString(1, userId);
                    pstmt.setString(2, userService.getLoggedInUser().getUserId());
                    int rowsAffected = pstmt.executeUpdate();
                    if(rowsAffected > 0)
                        return "redirect:/people";
                }
        }
        // Redirect the user if the comment adding is a success.
        // return "redirect:/people";

        // Redirect the user with an error message if there was an error.
        String message = URLEncoder.encode("Failed to (un)follow the user. Please try again.",
                StandardCharsets.UTF_8);
        return "redirect:/people?error=" + message;
    }

}
