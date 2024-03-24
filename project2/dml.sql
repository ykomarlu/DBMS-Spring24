--PostService class

    --getAllPostsOfFollowing
    --url: /
    --Overall Purpose: Gets all available posts from users that the loggedIn User follows

        --Purpose: Get the the postIds that the loggedIn user has hearted
        --select postId from heart where userId = ?
        --? in this case is replaced with the loggedIn user's user id

        --Purpose: Create a database instance with each postId being paired with the attribute of how many users hearted the post with each postId
        --select postId, count(userId) as count from heart group by postId

        --Purpose: Get all the postIds that are bookmarked by the the loggedIn user
        select postId from bookmark where userId = ?;
        --? in this case is replaced with the loggedIn user's user id

        --Purpose: Create database instance with attributes of each postId and how many comments were made to each postId
        select postId, count(userId) as count from comment group by postId;
        
        --Purpose: Create database instance with attributes necessary for creating a Post object where the User table is joined with the Post table
        --and also the userid for the corresponding users in the table must also be followed by the loggedIn user. Finally all the results from the query will b
        --sorted by latest to oldest postDate
        select p.userId as user, u.firstName as firstName, u.lastName as lastName, p.postId as postId, p.postText as content, p.postDate as postDate
        from post p join user u on p.userId = u.userId where u.userId in (select followeeUserId from follow where followerUserId = ? order by postDate desc);


    --newPost
    --url: /createpost
    --Overall Purpose: Creates a new post made by the loggedIn user with the passed in postContents

        --Purpose: Create a new post record with the intended text typed by the user so that it is associated with the logged in users
        --user id 
        insert into post (postText, userId) values (?, ?);
        --First "?" is substituted with the content of the post
        --Second "?" is substituted with the the userId of the loggedIn user

        --Purpose: Associate each hashtagged terms with the postId that contained it to enable hashtag functionalities
        --such as hashtag searching
        insert into hashtag values (?,?);
        --First "?" is substituted with the referenced postId 
        --Second "?" is substituted with each hashtagged term in the particular post

    --modifyBookmark
    --url: /post/{postId}/bookmark/{isAdd}
    
        --Purpose: If the isAdd property is true, a new bookmark record is created for the post with specific postId from the loggedIn user
        INSERT INTO bookmark VALUE (?,?);
        --The first "?" is substituted with the postId of the post that is being bookmarked
        --The second "?" is substituted with the userId of the loggedIn user


        --Purpose: The bookmark record of a specific post and made by the loggedIn user is deleted
        DELETE FROM bookmark WHERE postId = ? AND userId = ?;
        --First "?" is substituted with the specific postId
        --Second "?" is substituted with the userId of the loggedIn user

    --likePost
    --url: /post/{postId}/heart/{isAdd}

        --Purpose: 

    --getPostById
    --url: /post/{postId}
    --Overall Purpose: Retrieves a ExpandedPost object from the postId passed into it

        --Purpose: Retrieve every user in order to create user objects of each one
        select * from user;

        --Purpose: Retrieves a resultset if the loggedIn user has liked the post with the specified PostID 
        select postId from heart where userId = ? and postId = ?;
        --First "?" is substituted with the loggedIn user's user id
        --Second "?" is substituted with the particular postId of the post that is being liked\

        --Purpose: retrieve the number of users that liked the post with the provided postId
        select count(userId) as count from heart where postId = ?;
        --The "?" is replaced with the specified postId of the post referred to

        --Purpose: Retrieves a postId if that particular post has been bookmarked by the loggedIn user
        select postId from bookmark where userId = ? and postId = ?;
        --The first "?" is substituted with the loggedIn user's userId
        --The second "?" is substituted with the specified postId

        --Purpose: Retrieves the comments of the post from latest to oldest with the specified postId 
        select * from comment where postId = ? order by commentDate desc;
        --"?" is replaced by the specified postId

        --Purpose: Returns database instance combining the post and user attributes for the postId specified
        select p.userId as user, u.firstName as firstName, u.lastName as lastName,
        p.postId as postId, p.postText as content, p.postDate as postDate
        from post p join user u on p.userId = u.userId where p.postId = ? 
        -- "?" is substituted with specific post id 

    -- getPostsByUserIdOrderByDateDesc:
    -- url: /profile
    -- Overall Purpose: Specifically made for the profile page so that the posts on there
    -- would be ordered by latest to oldest postDate

        --Purpose: Retrieve all the posts that the user with the specified userId liked
        select postId from heart where userId = ?;
        --"?" is replaced with the LoggedIn user's userId

        --Purpose: Create database instance of each post alongside the number of likes that the post received
        select postId, count(userId) as count from heart group by postId;

        --Purpose: Retreive the postIds the loggedIn user bookmarked
        select postId from bookmark where userId = ?;
        --"?" is replaced with the loggedIn user's userId

        --Purpose: Create database instance where each post is associated with the number of comments that it received
        select postId, count(userId) as count from comment group by postId;

        --Purpose: Create Database instance where all post attributes and certain user attributes that match the userId of the 
        -- passed in userId and the posts are ordered in latest to oldest order
        SELECT p.*, u.firstName, u.lastName
        FROM post p JOIN user u ON p.userId = u.userId 
        WHERE p.userId = ? ORDER BY p.postDate DESC;
        -- "?" is substituted with the passed in userId in the method call
    
    -- getBookmarkedPosts:
    -- url: /bookmarks
    -- Overall Purpose: Retrieve all of the posts that have been bookmarked

        -- Purpose: Gets all the postIds that the loggedIn user has liked
        select postId from heart where userId = ?;
        -- "?" is substituted with the userid of the loggedIn user

        -- Purpose: Creates database instance associating each postId with the number of 
        -- users that liked that post.
        select postId, count(userId) as count from heart group by postId;

        -- Purpose: Retrieves all of the postIds that the loggedIn user bookmarked
        select postId from bookmark where userId = ?;

        -- Purpose: Created Database instance where each postId is associated with the number of comments
        -- that it received
        select postId, count(userId) as count from comment group by postId;

        -- Purpose: Created Database instance where the post and attributes are retrieved from all posts that are bookmarked by the loggedin user
        -- and the posts are ordered from latest to oldest
        select p.userId as user, u.firstName as firstName, u.lastName as lastName, p.postId as postId, p.postText as content, p.postDate as postDate
        from post p join (
        select postId from bookmark where userId = ?
        ) as b
        on p.postId = b.postId
        join user u
        on p.userId = u.userId
        order by postDate desc;
        -- "?" is replaced with the userId of the loggedin user

    -- newComment
    -- url: /post/{postId}/comment
    -- Overall Purpose: Allows the logged in user to create a new commment 

        -- Purpose: Creates a new comment containing the specifed text, for the specified post, and from the loggedIN user
        insert into comment (commentText, postId, userId) values (?, ?, ?);
        -- First "?" is substituted with the content provided by the user
        -- Second "?" is substituted with the postId that the user is commenting to
        -- Third "?" is substituted with the loggedIn user's userId

-- PeopleService:
    
    -- getFollowableUsers
    -- url: /people
    -- Overall Purpose: retrieve all the followableuser objects of the users that can be followed

        --Purpose: Retrieves the ids of all the followees (the user being followed) of the user who has an id which is the same as the passed in userId
        select followeeUserId as id from follow where followerUserId = ?;
        --"?" is substituted with the id of the user that is excluded which is the logged in user

        --Purpose: Retrieve all the attribtues from an database instance combining the user table and the records from the post table
        SELECT * FROM User 
        LEFT JOIN (
        select post.userId, post.postDate FROM post
        join ( 
        SELECT max(postDate) AS postDate, userId FROM post 
        GROUP BY userId
        ORDER BY postDate desc
        ) AS sub
        ON post.postDate = sub.postDate AND post.userId = sub.userId
        group by userId
        ) AS Post
        ON User.userId = Post.userId 
        having User.userId != ?;
        -- "?" is substituted with the userid of the loggedIN user

-- UserService

    -- getUserById
    -- url: /profile/{userid}
    -- Overall Purpose: Retrieves all the user attributes the bleong to the userId that is passed in
    SELECT * FROM user WHERE userId = ?;

