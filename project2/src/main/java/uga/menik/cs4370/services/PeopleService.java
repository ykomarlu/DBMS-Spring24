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
        // Write an SQL query to find the users that are not the current user.

        final String sql = "select f.followeeUserId as id, p.postDate as active from follow f join post p on f.followeeUserId=p.userId where f.followerUserId = ?;";
        final String sql2 = "select * from user where userId != ?";
        List<Integer> followList = new ArrayList<Integer>();
        List<String> activeDate = new ArrayList<String>();
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userIdToExclude);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    followList.add(rs.getInt("id"));
                    activeDate.add(rs.getString("active"));
                }
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        List<FollowableUser> userList = new ArrayList<FollowableUser>();
        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql2)) {

            pstmt.setString(1, userIdToExclude);
            int iterable = 0;

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String active = "Never";
                    if (activeDate.size() - 1 > iterable) {
                        active = activeDate.get(iterable);
                    }
                    userList.add(new FollowableUser(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName"), followList.contains(rs.getInt("userId")), active));
                    iterable++;
                }
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return userList;
    }

}
