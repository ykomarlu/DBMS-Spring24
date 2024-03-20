/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.models;

/**
 * Extends the User class to include a following status,
 * indicating whether the current session user follows this user.
 */
public class FollowableUser extends User {

    /**
     * Flag indicating whether the user is followed by the current session user.
     */
    private final boolean isFollowed;

    /**
     * This is the date and time that this user has last made a post.
     */
    private final String lastActiveDate;

    /**
     * Constructs a FollowableUser with specified details and follow status.
     *
     * @param userId           the unique identifier of the user
     * @param firstName        the first name of the user
     * @param lastName         the last name of the user
     * @param profileImageName the name of the profile image file for the user
     * @param isFollowed       the follow status of the user by the current session
     *                         user
     * @param lastActiveDate   the date and time that this user has last made a post.
     */
    public FollowableUser(String userId, String firstName, String lastName, String profileImageName,
            boolean isFollowed, String lastActiveDate) {
        super(userId, firstName, lastName, profileImageName);
        this.isFollowed = isFollowed;
        this.lastActiveDate = lastActiveDate;
    }

    /**
     * Constructs a FollowableUser with specified details and follow status.
     *
     * @param userId           the unique identifier of the user
     * @param firstName        the first name of the user
     * @param lastName         the last name of the user
     * @param isFollowed       the follow status of the user by the current session
     *                         user
     * @param lastActiveDate   the date and time that this user has last made a post.
     */
    public FollowableUser(String userId, String firstName, String lastName,
            boolean isFollowed, String lastActiveDate) {
        super(userId, firstName, lastName);
        this.isFollowed = isFollowed;
        this.lastActiveDate = lastActiveDate;
    }

    /**
     * Returns the follow status of the user.
     *
     * @return true if the user is followed by the current session user, false
     *         otherwise
     */
    public boolean isFollowed() {
        return isFollowed;
    }

    /**
     * Returns the last active date and time of the user.
     *
     * @return the date and time that this user has last made a post.
     */
    public String isLastActiveDate() {
        return lastActiveDate;
    }

}
