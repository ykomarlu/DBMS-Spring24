/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.models;

/**
 * Represents a comment made by a user.
 * Inherits all properties from BasicPost without adding new fields.
 */
public class Comment extends BasicPost {

    /**
     * Constructs a Comment with specified details, leveraging the BasicPost structure.
     *
     * @param postId     the unique identifier of the comment
     * @param content    the text content of the comment
     * @param postDate   the creation date of the comment
     * @param user       the user who made the comment
     */
    public Comment(String postId, String content, String postDate, User user) {
        super(postId, content, postDate, user);
    }
}
