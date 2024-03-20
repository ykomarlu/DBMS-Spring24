/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.models;

/**
 * Represents the basic structure of a post in the micro blogging platform.
 * This class serves as a base for both posts and comments.
 */
public class BasicPost {
    
    /**
     * Unique identifier for the post.
     */
    private final String postId;

    /**
     * Text content of the post.
     */
    private final String content;

    /**
     * Date when the post was created.
     */
    private final String postDate;

    /**
     * User who created the post.
     */
    private final User user;

    /**
     * Constructs a BasicPost with specified details.
     *
     * @param postId     the unique identifier of the post
     * @param content    the text content of the post
     * @param postDate   the creation date of the post
     * @param user       the user who created the post
     */
    public BasicPost(String postId, String content, String postDate, User user) {
        this.postId = postId;
        this.content = content;
        this.postDate = postDate;
        this.user = user;
    }

    /**
     * Returns the post ID.
     *
     * @return the post ID
     */
    public String getPostId() {
        return postId;
    }

    /**
     * Returns the content of the post.
     *
     * @return the content of the post
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the post creation date.
     *
     * @return the post creation date
     */
    public String getPostDate() {
        return postDate;
    }

    /**
     * Returns the user who created the post.
     *
     * @return the user who created the post
     */
    public User getUser() {
        return user;
    }
}
