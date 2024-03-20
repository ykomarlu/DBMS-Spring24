package uga.menik.cs4370.utility;

import java.util.ArrayList;
import java.util.List;

import uga.menik.cs4370.models.Comment;
import uga.menik.cs4370.models.ExpandedPost;
import uga.menik.cs4370.models.FollowableUser;
import uga.menik.cs4370.models.Post;
import uga.menik.cs4370.models.User;

public class Utility {

    public static List<FollowableUser> createSampleFollowableUserList() {
        List<FollowableUser> followableUsers = new ArrayList<>();
        followableUsers.add(new FollowableUser("1", "John", "Doe",
                true, "Mar 07, 2024, 10:54 PM"));
        followableUsers.add(new FollowableUser("2", "Jane", "Doe",
                false, "Mar 05, 2024, 11:00 AM"));
        followableUsers.add(new FollowableUser("3", "Alice", "Smith",
                true, "Mar 06, 2024, 09:30 AM"));
        followableUsers.add(new FollowableUser("4", "Bob", "Brown",
                false, "Mar 02, 2024, 08:15 PM"));
        return followableUsers;
    }

    public static List<Post> createSamplePostsListWithoutComments() {
        User user1 = new User("1", "John", "Doe");
        User user2 = new User("2", "Jane", "Doe");
        User user3 = new User("3", "Alice", "Smith");
        User user4 = new User("4", "Bob", "Brown");
        User user5 = new User("5", "Charlie", "Green");
        List<Post> postsWithoutComments = new ArrayList<>();
        postsWithoutComments.add(new Post("1", "Exploring Spring Boot features",
                "Mar 07, 2024, 10:54 PM", user1, 10, 4, false, false));
        postsWithoutComments.add(new Post("2", "Introduction to Microservices",
                "Mar 08, 2024, 11:00 AM", user2, 15, 6, true, true));
        postsWithoutComments.add(new Post("3", "Basics of Reactive Programming",
                "Mar 09, 2024, 09:30 AM", user3, 20, 3, true, false));
        return postsWithoutComments;
    }

    public static List<ExpandedPost> createSampleExpandedPostWithComments() {
        User user1 = new User("1", "John", "Doe");
        User user2 = new User("2", "Jane", "Doe");
        User user3 = new User("3", "Alice", "Smith");
        User user4 = new User("4", "Bob", "Brown");
        User user5 = new User("5", "Charlie", "Green");
        List<Comment> commentsForPost = new ArrayList<>();

        commentsForPost.add(new Comment("1", "Great insights, thanks for sharing!", 
            "Mar 07, 2024, 10:54 PM", user2));
        commentsForPost.add(new Comment("2", "I'm looking forward to trying this out.", 
            "Mar 08, 2024, 11:00 AM", user4));
        commentsForPost.add(new Comment("3", "Can you provide more examples in your next post?", 
            "Mar 09, 2024, 09:30 AM", user5));
        ExpandedPost postWithComments = new ExpandedPost("4", "Advanced Techniques " + 
            "in Spring Security", "Mar 10, 2024, 08:15 PM", user1, 25, 
            commentsForPost.size(), false, true, commentsForPost);
        return List.of(postWithComments);
    }

}
