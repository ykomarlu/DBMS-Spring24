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

import uga.menik.cs4370.models.FollowableUser;

/**
 * This service contains people related functions.
 */
@Service
public class PeopleService {
    private final DataSource dataSource;

    /**
     * See AuthInterceptor notes regarding dependency injection and
     * inversion of control.
     */
    @Autowired
    public PeopleService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    /**
     * This function should query and return all users that 
     * are followable. The list should not contain the user 
     * with id userIdToExclude.
     */
    public List<FollowableUser> getFollowableUsers(String userIdToExclude) {
        List<Integer> followList = new ArrayList<Integer>();
        List<FollowableUser> userList = new ArrayList<FollowableUser>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement followers = conn.prepareStatement("select followeeUserId as id from follow where followerUserId = ?");

                followers.setString(1, userIdToExclude);

                try (ResultSet rs = followers.executeQuery()) {
                    while (rs.next()) {
                        followList.add(rs.getInt("id"));
                    }
                }
                
            PreparedStatement excludedUser = conn.prepareStatement(
                    "SELECT * FROM User\n" + 
                    "LEFT JOIN ( \n" + 
                        "select post.userId, post.postDate FROM post\n" + 
                            "join (\n" + 
                            "SELECT max(postDate) AS postDate, userId FROM post\n" + 
                            "GROUP BY userId\n" + 
                            "ORDER BY postDate desc\n" + 
                            ") AS sub\n" + 
                        "ON post.postDate = sub.postDate AND post.userId = sub.userId\n" + 
                        "group by userId\n" + 
                    ") AS Post\n" + 
                    "ON User.userId = Post.userId\n" + 
                    "having User.userId != ?"
                    );
                excludedUser.setString(1, userIdToExclude);
                try (ResultSet rs = excludedUser.executeQuery()) {
                    while (rs.next()) {
                        String activeStatus = rs.getString("postDate") != null ? rs.getString("postDate") : "Never";
                        userList.add(new FollowableUser(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName"), followList.contains(rs.getInt("userId")), activeStatus));
                    }
                }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return userList;
    }

}
