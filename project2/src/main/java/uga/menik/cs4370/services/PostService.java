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
    private final User loggedInUser;

    /**
     * See AuthInterceptor notes regarding dependency injection and
     * inversion of control.
     */
    @Autowired
    public PostService(DataSource dataSource, User loggedInUser) {
        this.dataSource = dataSource;
        this.loggedInUser = loggedInUser;
    }
    
    /**
     * This function should query and return all users that 
     * are followable. The list should not contain the user 
     * with id userIdToExclude.
     */
    public List<Post> getPosts(String userIdToExclude) {

        final String sql = "select * from post";
        List<Post> postList = new ArrayList<Post>();
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // postList.add(new )
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
            postStatement.setString(2, loggedInUser.getUserId());

            int rowsAffected = postStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

}
