/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.models;

import java.util.List;

/**
 * Represents a post in its expanded form within the micro blogging platform.
 * An ExpandedPost includes comments.
 */
public class ExpandedPost extends Post {

    /**
     * A list of comments associated with the post.
     */
    private final List<Comment> comments;

    /**
     * Constructs an ExpandedPost with specified details including a list of comments.
     *
     * @param postId        the unique identifier of the post
     * @param content       the text content of the post
     * @param postDate      the creation date of the post
     * @param user          the user who created the post
     * @param heartsCount   the number of hearts (likes) the post has received
     * @param commentsCount the number of comments made on the post
     * @param isHearted     indicates whether the post is hearted by the current user
     * @param isBookmarked  indicates whether the post is bookmarked by the current user
     * @param comments      the list of comments made on the post
     */
    public ExpandedPost(String postId, String content, String postDate, User user, int heartsCount, int commentsCount, boolean isHearted, boolean isBookmarked, List<Comment> comments) {
        super(postId, content, postDate, user, heartsCount, commentsCount, isHearted, isBookmarked);
        this.comments = comments;
        this.isShowComents = true;
    }

    /**
     * Returns an unmodifiable view of the comments list, to prevent external modifications.
     *
     * @return an unmodifiable list of comments
     */
    public List<Comment> getComments() {
        return List.copyOf(comments);
    }
}
