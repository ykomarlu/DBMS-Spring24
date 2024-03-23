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
import uga.menik.cs4370.models.ExpandedPost;
import uga.menik.cs4370.models.Post;
import uga.menik.cs4370.models.Comment;

@Service
@SessionScope
public class PostService {
    private final DataSource dataSource;
    private final User loggedInUser;
    private int authedUserId;

    /**
     * See AuthInterceptor notes regarding dependency injection and
     * inversion of control.
     */
    @Autowired
    public PostService(DataSource dataSource, UserService userService) {
        this.dataSource = dataSource;
        this.loggedInUser = userService.getLoggedInUser();
        this.authedUserId = Integer.parseInt(userService.getLoggedInUser().getUserId());
    }
    
    /**
    * Gets a post by its ID
     */
    public ExpandedPost getPostById(int postId) {
        boolean isHearted = false;
        boolean isBookmarked = false;

        int heartCount = 0;
        int commentCount = 0;

        List<Comment> comments = new ArrayList<Comment>();
        List<User> users = new ArrayList<User>();

        ExpandedPost post = null;

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement heartsList = conn.prepareStatement("select postId from heart where userId = ? and postId = ?");
                heartsList.setInt(1, authedUserId);
                heartsList.setInt(2, postId);
                try (ResultSet rs = heartsList.executeQuery()) {
                    if (rs.next()) {
                        isHearted = true;
                    }
                }

            PreparedStatement heartsCount = conn.prepareStatement("select count(userId) as count from heart where postId = ?");
                heartsCount.setInt(1, postId);
                try (ResultSet rs = heartsCount.executeQuery()) {
                    if (rs.next()) {
                        heartCount = Integer.parseInt(rs.getString("count"));
                    }
                }

            PreparedStatement bookmarks = conn.prepareStatement("select postId from bookmark where userId = ? and postId = ?");
                bookmarks.setInt(1, authedUserId);
                bookmarks.setInt(2, postId);
                try (ResultSet rs = bookmarks.executeQuery()) {
                    if (rs.next()) {
                        isBookmarked = true;
                    }
                }

            PreparedStatement commentCountStmt = conn.prepareStatement("select * from comment where postId = ?");
                commentCountStmt.setInt(1, postId);
                try (ResultSet rs = commentCountStmt.executeQuery()) {
                    while (rs.next()) {
                        User currentUser = null;
                        for (int i = 0; i < users.size(); i++) {
                            if (users.get(i).getUserId() == rs.getString("userId")) {
                                currentUser = users.get(i);
                            }
                        }
                        comments.add(new Comment(String.valueOf(postId), rs.getString("commentText"), rs.getString("commentDate"), currentUser));
                    }
                }


            PreparedStatement posts = conn.prepareStatement("select p.userId as user, u.firstName as firstName, u.lastName as lastName, p.postId as postId, p.postText as content, p.postDate as postDate from post p join user u on p.userId = u.userId where p.postId = ?");
                posts.setInt(1, postId);
                try (ResultSet rs = posts.executeQuery()) {
                    if (rs.next()) {
                        post = new ExpandedPost(
                                rs.getString("postId"), 
                                rs.getString("content"), 
                                rs.getString("postDate"), 
                                new User(rs.getString("user"), rs.getString("firstName"), rs.getString("lastName")), 
                                heartCount, 
                                commentCount, 
                                isHearted,
                                isBookmarked,
                                comments
                            );
                    }
                }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
        return post;
    }
    
    /**
    * Gets all available posts
     */
    public List<Post> getAllPosts() {
        List<String> authedUserHeartedIds = new ArrayList<String>();
        List<String> authedUserBookmarkedIds = new ArrayList<String>();
        List<Integer> heartCountList = new ArrayList<Integer>();
        List<Integer> heartCountIdList = new ArrayList<Integer>();
        List<Integer> commentCountList = new ArrayList<Integer>();
        List<Integer> commentCountIdList = new ArrayList<Integer>();

        List<Post> postList = new ArrayList<Post>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement heartsList = conn.prepareStatement("select postId from heart where userId = ?");
                heartsList.setInt(1, authedUserId);
                try (ResultSet rs = heartsList.executeQuery()) {
                    while (rs.next()) {
                        authedUserHeartedIds.add(rs.getString("postId"));
                    }
                }

            PreparedStatement heartsCount = conn.prepareStatement("select postId, count(userId) as count from heart group by postId");
                try (ResultSet rs = heartsCount.executeQuery()) {
                    while (rs.next()) {
                        heartCountList.add(rs.getInt("count"));
                        heartCountIdList.add(rs.getInt("postId"));
                    }
                }

            PreparedStatement bookmarks = conn.prepareStatement("select postId from bookmark where userId = ?");
                bookmarks.setInt(1, authedUserId);
                try (ResultSet rs = bookmarks.executeQuery()) {
                    while (rs.next()) {
                        authedUserBookmarkedIds.add(rs.getString("postId"));
                    }
                }

            PreparedStatement commentCount = conn.prepareStatement("select postId, count(userId) as count from comment group by postId");
                try (ResultSet rs = commentCount.executeQuery()) {
                    while (rs.next()) {
                        commentCountList.add(rs.getInt("count"));
                        commentCountIdList.add(rs.getInt("postId"));
                    }
                }


            PreparedStatement posts = conn.prepareStatement("select p.userId as user, u.firstName as firstName, u.lastName as lastName, p.postId as postId, p.postText as content, p.postDate as postDate from post p join user u on p.userId = u.userId order by postDate");
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
                                authedUserHeartedIds.contains(rs.getString("postId")),
                                authedUserBookmarkedIds.contains(rs.getString("postId"))
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
            postStatement.setInt(2, authedUserId);

            int rowsAffected = postStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * 
     * @param postId
     * @param isAdd
     * @return
     * @throws SQLException
     */

     public boolean modifyBookmark(int postId, boolean isAdd) throws SQLException {
// inserting new value into the heart table so that logged in user can like a 
        // post with a specific postId
        final String alreadyBookmarked = "Select bookmark.postId from bookmark where bookmark.userId = (?)";
        // Query that deletes a tuple in the heart table that has the specific postId and userId looked for
        final String removeBookMark = "delete from bookmark where bookmark.postId = (?) and bookmark.userId = (?)";
        final String newBookMark = "insert into bookmark value (?,?)";
        // Query that retrieves all the postids that the logged in user has liked
        
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps2 = conn.prepareStatement(newBookMark); 
        PreparedStatement ps = conn.prepareStatement(alreadyBookmarked); PreparedStatement ps3 = conn.prepareStatement(removeBookMark)) {

            ps2.setInt(1, postId);
            ps2.setInt(2, Integer.parseInt(loggedInUser.getUserId()));
            ps.setInt(1, Integer.parseInt(loggedInUser.getUserId()));
            ps3.setInt(1, postId);
            ps3.setInt(2, Integer.parseInt(loggedInUser.getUserId()));

            ResultSet rs = ps.executeQuery();
            List<Integer> postIds = new ArrayList<>();
            
            while (rs.next()) {
                postIds.add(rs.getInt("postId"));
            }

            if (isAdd && !postIds.contains(postId)) {
                ps2.executeQuery();
                return true;
            }

            else if (!isAdd && postIds.contains(postId)) {
                ps3.executeQuery();
                return true;
            }

            else {
                throw new SQLException();
            }
        }

     }

    /**
     * Allows the logged in user to like posts
     */

     public boolean likePost (int postId, boolean isAdd) throws SQLException {
        // inserting new value into the heart table so that logged in user can like a 
        // post with a specific postId
        final String alreadyLiked = "Select heart.postId from heart where heart.userId = (?)";
        // Query that deletes a tuple in the heart table that has the specific postId and userId looked for
        final String removeLike = "delete from heart where heart.postId = (?) and heart.userId = (?)";
        final String newLike = "insert into heart value (?,?)";
        // Query that retrieves all the postids that the logged in user has liked
        
        try (Connection conn = dataSource.getConnection(); PreparedStatement ps2 = conn.prepareStatement(newLike); 
        PreparedStatement ps = conn.prepareStatement(alreadyLiked); PreparedStatement ps3 = conn.prepareStatement(removeLike)) {

            ps2.setInt(1, postId);
            ps2.setInt(2, Integer.parseInt(loggedInUser.getUserId()));
            ps.setInt(1, Integer.parseInt(loggedInUser.getUserId()));
            ps3.setInt(1, postId);
            ps3.setInt(2, Integer.parseInt(loggedInUser.getUserId()));

            ResultSet rs = ps.executeQuery();
            List<Integer> postIds = new ArrayList<>();
            
            while (rs.next()) {
                postIds.add(rs.getInt("postId"));
            }

            if (isAdd && !postIds.contains(postId)) {
                ps2.executeQuery();
                return true;
            }

            else if (!isAdd && postIds.contains(postId)) {
                ps3.executeQuery();
                return true;
            }

            else {
                throw new SQLException();
            }
        }

        // if a post is already liked then the user should not be allowed to relike it
     }
}
