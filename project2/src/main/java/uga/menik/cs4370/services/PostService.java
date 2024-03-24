/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import uga.menik.cs4370.models.User;
import uga.menik.cs4370.utility.Utility;
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
        this.authedUserId = Integer.parseInt(this.loggedInUser.getUserId());
    }

    /**
     * Creates a new post
     * Returns true if new post is successful.
     */
    public boolean newComment(int postId, String content) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement commentStatement = 
            conn.prepareStatement("insert into comment (commentText, postId, userId) values (?, ?, ?)");

            commentStatement.setString(1, content);
            commentStatement.setInt(2, postId);
            commentStatement.setInt(3, authedUserId);

            int rowsAffected = commentStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            return false;
        }
    }

    /**
     * Creates a new post
     * Returns true if new post is successful.
     */
    public boolean newPost(String content) throws SQLException {
        
        //inserting into post
        final String newPost = "insert into post (postText, userId) values (?, ?)";

        try (Connection conn = dataSource.getConnection(); PreparedStatement postStatement = conn.prepareStatement(newPost, Statement.RETURN_GENERATED_KEYS)) {
            postStatement.setString(1, content);
            postStatement.setInt(2, authedUserId);
            
            int rowsAffected = postStatement.executeUpdate();
            ResultSet rs = postStatement.getGeneratedKeys();
            rs.next();
            String postId = rs.getInt(1)+"";
            //inserting into hashtag
        ArrayList<String> wordsWithHashtags = new ArrayList<>();
        String words[] = content.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (words[i].startsWith("#")) {
                String hashtag = words[i].substring(1);
                wordsWithHashtags.add(hashtag);
            }
        }
        String hashInsert = "insert into hashtag values (?,?)";
          try (Connection conn2 = dataSource.getConnection(); PreparedStatement postStatement2 = conn.prepareStatement(hashInsert)){
            for (String hashtag : wordsWithHashtags){
                postStatement2.setString(1, hashtag);
                postStatement2.setString(2, postId);
                postStatement2.executeUpdate();
            }
          }
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
        try (Connection conn = dataSource.getConnection()) {
            if (isAdd) {
                PreparedStatement insert = conn.prepareStatement("INSERT INTO bookmark VALUE (?,?)");

                insert.setInt(1, postId);
                insert.setInt(2, authedUserId);

                insert.executeUpdate();
            } else {
                PreparedStatement delete = conn.prepareStatement("DELETE FROM bookmark WHERE postId = ? AND userId = ?");

                delete.setInt(1, postId);
                delete.setInt(2, authedUserId);

                delete.executeUpdate();
            }
            return true;
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            return false;
        }
     }

    /**
    * Allows the logged in user to like posts
    */
     public boolean likePost (int postId, boolean isAdd) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (isAdd) {
                PreparedStatement addLike = conn.prepareStatement("INSERT INTO heart VALUE (?,?)"); 

                addLike.setInt(1, postId);
                addLike.setInt(2, authedUserId);

                addLike.executeUpdate();
            } else {
                PreparedStatement removeLike = conn.prepareStatement("DELETE FROM heart WHERE postId = ? AND userId = ?");

                removeLike.setInt(1, postId);
                removeLike.setInt(2, authedUserId);

                removeLike.executeUpdate();
            }

            return true;
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            return false;
        }
     }

    /*
    * this is for the profile thing.
    */
    public List<Post> getBookmarkedPosts() {
        List<String> authedUserHeartedIds = new ArrayList<String>();
        List<String> authedUserBookmarkedIds = new ArrayList<String>();
        List<Integer> heartCountList = new ArrayList<Integer>();
        List<Integer> heartCountIdList = new ArrayList<Integer>();
        List<Integer> commentCountList = new ArrayList<Integer>();
        List<Integer> commentCountIdList = new ArrayList<Integer>();
        
        List<Post> posts = new ArrayList<>();

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

            PreparedStatement postsByUserId = conn.prepareStatement(
                    "select p.userId as user, u.firstName as firstName, u.lastName as lastName, p.postId as postId, p.postText as content, p.postDate as postDate\n" +
                    "from post p\n" +
                    "join (\n" +
                        "select postId from bookmark where userId = ?\n" +
                    ") as b\n" +
                    "on p.postId = b.postId\n" +
                    "join user u \n" +
                    "on p.userId = u.userId\n" +
                    "order by postDate desc"
                );
            postsByUserId.setInt(1, authedUserId);
            
            try (ResultSet rs = postsByUserId.executeQuery()) {
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

                    Post post = new Post(
                        rs.getString("postId"), 
                        rs.getString("content"), 
                        Utility.formatDate(rs.getString("postDate")),
                        new User(rs.getString("user"), rs.getString("firstName"), rs.getString("lastName")), 
                        hearts, 
                        comments, 
                        authedUserHeartedIds.contains(rs.getString("postId")),
                        authedUserBookmarkedIds.contains(rs.getString("postId"))
                    );

                    posts.add(post);
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return posts;
    }

    /*
    * this is for the profile thing.
    */
    public List<Post> getPostsByUserIdOrderByDateDesc(String userId) {
        List<String> authedUserHeartedIds = new ArrayList<String>();
        List<String> authedUserBookmarkedIds = new ArrayList<String>();
        List<Integer> heartCountList = new ArrayList<Integer>();
        List<Integer> heartCountIdList = new ArrayList<Integer>();
        List<Integer> commentCountList = new ArrayList<Integer>();
        List<Integer> commentCountIdList = new ArrayList<Integer>();
        
        List<Post> posts = new ArrayList<>();

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

            PreparedStatement postsByUserId = conn.prepareStatement(
                    "SELECT p.*, u.firstName, u.lastName " +
                     "FROM post p " +
                     "JOIN user u ON p.userId = u.userId " +
                     "WHERE p.userId = ? " +
                     "ORDER BY p.postDate DESC"
                );
            postsByUserId.setString(1, userId);
            
            try (ResultSet rs = postsByUserId.executeQuery()) {
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

                    User user = new User(
                        rs.getString("userId"),
                        rs.getString("firstName"),
                        rs.getString("lastName")
                    );

                    Post post = new Post(
                        rs.getString("postId"),
                        rs.getString("postText"),
                        Utility.formatDate(rs.getString("postDate")),
                        user,
                        hearts,
                        comments, 
                        authedUserHeartedIds.contains(rs.getString("postId")),
                        authedUserBookmarkedIds.contains(rs.getString("postId"))
                    );

                    posts.add(post);
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return posts;
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
            PreparedStatement usersList = conn.prepareStatement("select * from user");
                try (ResultSet rs = usersList.executeQuery()) {
                    while (rs.next()) {
                        users.add(new User(String.valueOf(rs.getInt("userId")), rs.getString("firstName"), rs.getString("lastName")));
                    }
                }

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

            PreparedStatement commentCountStmt = conn.prepareStatement("select * from comment where postId = ? order by commentDate desc");
                commentCountStmt.setInt(1, postId);
                try (ResultSet rs = commentCountStmt.executeQuery()) {
                    while (rs.next()) {
                        User currentUser = null;
                        for (int i = 0; i < users.size(); i++) {
                            if (users.get(i).getUserId().equals(rs.getString("userId"))) {
                                currentUser = users.get(i);
                            }
                        }
                        comments.add(new Comment(String.valueOf(postId), rs.getString("commentText"), Utility.formatDate(rs.getString("commentDate")), currentUser));
                    }
                    commentCount = comments.size();
                }


            PreparedStatement posts = conn.prepareStatement(
                        "select p.userId as user, u.firstName as firstName, u.lastName as lastName, \n" +
                        "p.postId as postId, p.postText as content, p.postDate as postDate \n" +
                        "from post p \n" +
                        "join user u \n" +
                        "on p.userId = u.userId \n" +
                        "where p.postId = ?"
                    );
                posts.setInt(1, postId);
                try (ResultSet rs = posts.executeQuery()) {
                    if (rs.next()) {
                        post = new ExpandedPost(
                                rs.getString("postId"), 
                                rs.getString("content"), 
                                Utility.formatDate(rs.getString("postDate")), 
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


            PreparedStatement posts = conn.prepareStatement(
                    "select p.userId as user, u.firstName as firstName, u.lastName as lastName, p.postId as postId, p.postText as content, p.postDate as postDate\n" +
                    "from post p \n" +
                    "join user u \n" +
                    "on p.userId = u.userId \n" +
                    "order by postDate desc");
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
    * Gets all available posts from followers
    */
    public List<Post> getAllPostsOfFollowing() {
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


            PreparedStatement posts = conn.prepareStatement(
                    "select p.userId as user, u.firstName as firstName, u.lastName as lastName, p.postId as postId, p.postText as content, p.postDate as postDate\n" +
                    "from post p \n" +
                    "join user u \n" +
                    "on p.userId = u.userId \n" +
                    "where u.userId in (select followeeUserId from follow where followerUserId = ?) \n" +
                    "order by postDate desc"
                    );
                    posts.setInt(1, authedUserId);
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
                                Utility.formatDate(rs.getString("postDate")), 
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
}
