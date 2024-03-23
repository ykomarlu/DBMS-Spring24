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
        List<String> activeDate = new ArrayList<String>();
        List<FollowableUser> userList = new ArrayList<FollowableUser>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement followers = conn.prepareStatement("select followeeUserId as id from follow where followerUserId = ?");

                followers.setString(1, userIdToExclude);

                try (ResultSet rs = followers.executeQuery()) {
                    while (rs.next()) {
                        followList.add(rs.getInt("id"));
                    }
                }

                PreparedStatement active = conn.prepareStatement("select followeeUserId as id from follow where followerUserId = ?");

                active.setString(1, userIdToExclude);

                try (ResultSet rs = active.executeQuery()) {
                    while (rs.next()) {
                        activeDate.add(rs.getString("active"));
                    }
                }
                
            PreparedStatement excludedUser = conn.prepareStatement("SELECT * FROM User LEFT JOIN Post ON User.userId = Post.userId where User.userId != ?");
                excludedUser.setString(1, userIdToExclude);
                int iterable = 0;

                try (ResultSet rs = excludedUser.executeQuery()) {
                    while (rs.next()) {
                        String activeStatus = "Never";
                        if (activeDate.size() - 1 > iterable) {
                            activeStatus = activeDate.get(iterable);
                        }
                        userList.add(new FollowableUser(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName"), !followList.contains(rs.getInt("userId")), activeStatus));
                        iterable++;
                    }
                }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        return userList;
    }

}
