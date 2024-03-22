/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import uga.menik.cs4370.models.User;
import uga.menik.cs4370.models.Post;

@Service
@SessionScope
public class PostService {
    private final DataSource dataSource;
    private final UserService userService;

    private int loggedInUserId;

    /**
     * See AuthInterceptor notes regarding dependency injection and
     * inversion of control.
     */
    @Autowired
    public PostService(DataSource dataSource, UserService userService) {
        this.dataSource = dataSource;
        this.userService = userService;
        this.loggedInUserId = Integer.parseInt(userService.getLoggedInUser().getUserId());
    }
    
    /**
    * Gets all available posts
     */
    public List<Post> getAllPosts() {
        final String heartIdListSQL = "select postId from heart where userId = ?";
        final String heartCountSQL = "select postId, count(userId) as count from heart group by postId";

        final String bookmarkIdListSQL = "select postId from bookmark where userId = ?";
        final String commentCountSQL = "select postId, count(userId) as count from comment group by postId";

        final String postSelectSQL = 
        "select p.userId as user, u.firstName as firstName, u.lastName as lastName, p.postId as postId, p.postText as content, p.postDate as postDate from post p join user u on p.userId = u.userId group by postId;";  

        List<String> heartedIds = new ArrayList<String>();
        List<String> bookmarkedIds = new ArrayList<String>();

        List<Integer> heartCountList = new ArrayList<Integer>();
        List<Integer> heartCountIdList = new ArrayList<Integer>();
        List<Integer> commentCountList = new ArrayList<Integer>();
        List<Integer> commentCountIdList = new ArrayList<Integer>();

        List<Post> postList = new ArrayList<Post>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement heartsList = conn.prepareStatement(heartIdListSQL);
            heartsList.setInt(1, loggedInUserId);
            try (ResultSet rs = heartsList.executeQuery()) {
                while (rs.next()) {
                    heartedIds.add(rs.getString("postId"));
                }
            }

            PreparedStatement heartsCount = conn.prepareStatement(heartCountSQL);
            try (ResultSet rs = heartsCount.executeQuery()) {
                while (rs.next()) {
                    heartCountList.add(rs.getInt("count"));
                    heartCountIdList.add(rs.getInt("postId"));
                }
            }

            PreparedStatement bookmarks = conn.prepareStatement(bookmarkIdListSQL);
            bookmarks.setInt(1, loggedInUserId);
            try (ResultSet rs = bookmarks.executeQuery()) {
                while (rs.next()) {
                    bookmarkedIds.add(rs.getString("postId"));
                }
            }

            PreparedStatement commentCount = conn.prepareStatement(commentCountSQL);
            try (ResultSet rs = commentCount.executeQuery()) {
                while (rs.next()) {
                    commentCountList.add(rs.getInt("count"));
                    commentCountIdList.add(rs.getInt("postId"));
                }
            }


            PreparedStatement posts = conn.prepareStatement(postSelectSQL);
            try (ResultSet rs = posts.executeQuery()) {
                while (rs.next()) {
                    int hearts = 0;
                    int comments = 0;

                    for (int i = 0; i < heartCountList.size(); i++) {
                        if (heartCountIdList.get(i) == Integer.parseInt(rs.getString("postId"))) {
                            hearts = heartCountList.get(i);
                        }
                    }

                    for (int i = 0; i < commentCountList.size(); i++) {
                        if (commentCountIdList.get(i) == Integer.parseInt(rs.getString("postId"))) {
                            comments = commentCountList.get(i);
                        }
                    }

                    postList.add(new Post(
                            rs.getString("postId"), 
                            rs.getString("content"), 
                            rs.getString("postDate"), 
                            new User(rs.getString("user"), rs.getString("firstName"), rs.getString("lastName")), 
                            hearts, 
                            comments, 
                            heartedIds.contains(rs.getString("postId")),
                            bookmarkedIds.contains(rs.getString("postId"))
                        )
                    );
                }
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return postList;
    }

    /**
     * Creates a new post
     * Returns true if new post is successful.
     */
    public boolean newPost(String content) throws SQLException {
        final String newPost = "insert into post (postText, userId) values (?, ?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement postStatement = conn.prepareStatement(newPost)) {
            postStatement.setString(1, content);
            postStatement.setInt(2, loggedInUserId);

            int rowsAffected = postStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
