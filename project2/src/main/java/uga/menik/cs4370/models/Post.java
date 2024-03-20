/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.models;

/**
 * Represents a user's post in the micro blogging platform,
 * extending the BasicPost with additional features.
 */
public class Post extends BasicPost {
    
    /**
     * The number of hearts (likes) the post has received.
     */
    private final int heartsCount;
    
    /**
     * The number of comments the post has received.
     */
    private final int commentsCount;
    
    /**
     * Flag indicating whether the post is hearted by the current user.
     */
    private final boolean isHearted;
    
    /**
     * Flag indicating whether the post is bookmarked by the current user.
     */
    private final boolean isBookmarked;

    /**
     * Flag to specify whether to show comments or not.
     */
    protected boolean isShowComents;

    /**
     * Constructs a Post with specified details including information from BasicPost.
     *
     * @param postId         the unique identifier of the post
     * @param content        the text content of the post
     * @param postDate       the creation date of the post
     * @param user           the user who created the post
     * @param heartsCount    the number of hearts (likes) the post has received
     * @param commentsCount  the number of comments the post has received
     * @param isHearted      whether the post is hearted by the current user
     * @param isBookmarked   whether the post is bookmarked by the current user
     */
    public Post(String postId, String content, String postDate, User user, int heartsCount, int commentsCount, boolean isHearted, boolean isBookmarked) {
        super(postId, content, postDate, user);
        this.heartsCount = heartsCount;
        this.commentsCount = commentsCount;
        this.isHearted = isHearted;
        this.isBookmarked = isBookmarked;
        this.isShowComents = false;
    }

    /**
     * Returns the number of hearts (likes) the post has received.
     *
     * @return the number of hearts
     */
    public int getHeartsCount() {
        return heartsCount;
    }

    /**
     * Returns the number of comments the post has received.
     *
     * @return the number of comments
     */
    public int getCommentsCount() {
        return commentsCount;
    }

    /**
     * Returns whether the post is hearted by the current user.
     *
     * @return true if the post is hearted, false otherwise
     */
    public boolean getHearted() {
        return isHearted;
    }

    /**
     * Returns whether the post is bookmarked by the current user.
     *
     * @return true if the post is bookmarked, false otherwise
     */
    public boolean isBookmarked() {
        return isBookmarked;
    }
}
