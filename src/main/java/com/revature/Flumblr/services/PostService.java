package com.revature.Flumblr.services;

import com.revature.Flumblr.repositories.BookmarksRepository;
import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.repositories.PostShareRepository;
import com.revature.Flumblr.repositories.PostVoteRepository;
import com.revature.Flumblr.repositories.UserRepository;
import com.revature.Flumblr.utils.custom_classes.SortedPost;
import com.revature.Flumblr.utils.custom_exceptions.ResourceConflictException;
import com.revature.Flumblr.utils.custom_exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.revature.Flumblr.dtos.responses.CommentResponse;
import com.revature.Flumblr.dtos.responses.PostResponse;
import com.revature.Flumblr.dtos.responses.UserResponse;
import com.revature.Flumblr.entities.User;
import lombok.AllArgsConstructor;

import com.revature.Flumblr.entities.Bookmark;
import com.revature.Flumblr.entities.Comment;
import com.revature.Flumblr.entities.CommentMention;
import com.revature.Flumblr.entities.CommentVote;
import com.revature.Flumblr.entities.Follow;
import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.PostMention;
import com.revature.Flumblr.entities.PostShare;
import com.revature.Flumblr.entities.PostVote;
import com.revature.Flumblr.entities.Tag;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final S3StorageService s3StorageService;
    private final TagService tagService;
    private final PostVoteRepository postVoteRepository;
    private final CommentVoteService commentVoteService;
    private final BookmarksRepository bookmarksRepository;
    private final PostShareRepository postShareRepository;
    private final NotificationService notificationService;
    private final NotificationTypeService notificationTypeService;

    public PostResponse findByIdResponse(String postId, String requesterId) {
        Optional<Post> userPost = this.postRepository.findById(postId);
        if (userPost.isEmpty())
            throw new ResourceNotFoundException("Post(" + postId + ") Not Found");
        User requestUser = userService.findById(requesterId);
        return postResponseFromPost(userPost.get(), requestUser, makeFollowedIds(requestUser));
    }

    public List<PostResponse> postResponsesFromPosts(List<Post> posts, User requestUser) {
        List<PostResponse> resPosts = new ArrayList<PostResponse>();
        Set<String> followedIds = makeFollowedIds(requestUser);
        for (Post post : posts) {
            resPosts.add(postResponseFromPost(post, requestUser, followedIds));
        }
        return resPosts;
    }

    private Set<String> makeFollowedIds(User requestUser) {
        Set<String> followedIds = new HashSet<String>();
        for (Follow follow : requestUser.getFollows()) {
            followedIds.add(follow.getFollow().getId());
        }
        followedIds.add(requestUser.getId());
        return followedIds;
    }

    public PostResponse postResponseFromPost(Post post, User requestUser, Set<String> followedIds) {
        Set<PostVote> postVotes = post.getPostVotes();
        int upVotes = 0;
        for (PostVote postVote : postVotes) {
            if (postVote.isVote())
                upVotes++;
        }
        List<CommentResponse> comments = new ArrayList<CommentResponse>();
        for (Comment comment : post.getComments()) {
            CommentVote commentVote = commentVoteService.findByUserAndComment(requestUser, comment);
            CommentResponse commentResponse = new CommentResponse(comment);
            commentResponse.setUserVote(commentVote);
            commentResponse.setMentions(getMentions(comment));
            comments.add(commentResponse);
        }

        PostResponse response = new PostResponse(post);
        PostVote postVote = postVoteRepository.findByUserAndPost(requestUser, post).orElse(null);
        Bookmark bookmark = bookmarksRepository.findByUserAndPost(requestUser, post).orElse(null);
        PostShare postShare = postShareRepository.findByUserAndPost(requestUser, post).orElse(null);

        response.setSharedBy(findUsersForSharesAndFollowedIds(post, followedIds));
        response.setShareCount(post.getPostShares().size());
        response.setUserVote(postVote);
        response.setBookmarked(bookmark);
        response.setShared(postShare);
        response.setUpVotes(upVotes);
        response.setDownVotes(postVotes.size() - upVotes);
        response.setComments(comments);
        response.setMentions(getMentions(post));
        return response;
    }

    public List<PostResponse> getUserBookmarkedPosts(String userId) {
        User user = userService.findById(userId);
        List<Post> posts = postRepository.findByBookmarksUser(user);
        return postResponsesFromPosts(posts, user);
    }

    public List<PostResponse> getFollowing(String userId, int page) {
        User user = userService.findById(userId);
        List<User> following = new ArrayList<User>();
        for (Follow follow : user.getFollows()) {
            following.add(follow.getFollow());
        }
        List<Post> posts = postRepository.findPostsAndSharesForUserIn(following,
                PageRequest.of(page, 20, Sort.by("createTime").descending()));

        return postResponsesFromPosts(posts, user);
    }

    public List<PostResponse> getFeed(int page, String requesterId) {
        List<Post> posts = postRepository.findAllBy(PageRequest.of(page, 20, Sort.by("createTime").descending()));
        return postResponsesFromPosts(posts, userService.findById(requesterId));
    }

    public List<PostResponse> findByTag(List<String> tags, int page, String requesterId) {
        List<Post> posts = postRepository.findByTagsNameIn(tags,
                PageRequest.of(page, 20, Sort.by("createTime").descending()));
        return postResponsesFromPosts(posts, userService.findById(requesterId));
    }

    // return list of users that user follows who also shared the post
    public List<UserResponse> findUsersForSharesAndFollowedIds(Post post, Set<String> followedIds) {
        List<UserResponse> usersShared = new ArrayList<UserResponse>();
        for (PostShare postShare : post.getPostShares()) {
            User user = postShare.getUser();
            if (followedIds.contains(user.getId())) {
                usersShared.add(new UserResponse(user));
            }
        }
        return usersShared;
    }

    public List<PostResponse> getUserPosts(String userId, String requesterId) {
        List<Post> userPosts = this.postRepository.findPostsAndSharesByUserId(userId);
        return postResponsesFromPosts(userPosts, userService.findById(requesterId));
    }

    public Post findById(String postId) {
        Optional<Post> userPost = this.postRepository.findById(postId);
        if (userPost.isEmpty())
            throw new ResourceNotFoundException("Post(" + postId + ") Not Found");
        return userPost.get();
    }

    public String getPostOwner(String postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + postId + " was not found"));
        return post.getUser().getId();
    }

    public void deletePost(String postId) {
        try {
            Optional<Post> postOptional = postRepository.findById(postId);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                if (post.getS3Url() != null) {
                    s3StorageService.deleteFileFromS3Bucket(post.getS3Url());
                }

                postRepository.deleteById(postId);
            } else {
                throw new ResourceNotFoundException("Post with id " + postId + " was not found");
            }

        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Post with id " + postId + " was not found");
        }
    }

    public void createPost(MultipartHttpServletRequest req, String fileUrl, String userId) {

        Optional<User> userOpt = userRepository.findById(userId);

        User user = userOpt.get();

        String message = req.getParameter("message");

        if (message == null && fileUrl == null) {
            throw new ResourceConflictException("Message or media required!");
        }
        String mediaType = req.getParameter("mediaType");
        // if (mediaType == null) {
        // throw new FileNotUploadedException("Media Type can not be empty!");
        // }

        String[] tagsArray = req.getParameterValues("tags");
        Set<Tag> tagsList = new HashSet<>();

        if (tagsArray != null) {

            for (String tagNames : tagsArray) {
                Tag tag = tagService.findByName(tagNames);

                tagsList.add(tag);
            }
        }

        Post post = new Post(message, mediaType, fileUrl, user, tagsList);

        String[] mentionsArray = req.getParameterValues("mentions");
        Set<PostMention> mentionsList = new HashSet<PostMention>();
        if (mentionsArray != null) {

            for (String mentionName : mentionsArray) {
                Optional<User> mentionedOpt = userRepository.findByUsername(mentionName);
                if (mentionedOpt.isPresent()) {
                    User mentioned = mentionedOpt.get();
                    notificationService.createNotification(user.getUsername() + " mentioned you in a post",
                            "post:" + post.getId(), mentioned,
                            notificationTypeService.findByName("postMention"));

                    mentionsList.add(new PostMention(mentioned, post));
                }
            }
        }
        post.setPostMentions(mentionsList);

        postRepository.save(post);
    }

    public PostResponse updatePost(String postId, MultipartHttpServletRequest req, String fileUrl) {
        Post post = this.findById(postId);
        User user = post.getUser();
        String newMessage = req.getParameter("message");
        String newMediaType = req.getParameter("mediaType");
        String[] newTagsArray = req.getParameterValues("tags");
        String existingFileUrl = post.getS3Url();

        Set<PostMention> mentions = post.getPostMentions();
        String[] newMentionArray = req.getParameterValues("mentions");
        Iterator<PostMention> mentionIter = mentions.iterator();
        if (newMentionArray == null)
            mentions.clear();
        else {
            Set<String> newMentions = new HashSet<String>(
                    Arrays.asList(newMentionArray));
            while (mentionIter.hasNext()) {
                String existingUsername = mentionIter.next().getUser().getUsername();
                if (!newMentions.contains(existingUsername))
                    mentionIter.remove();
                else
                    newMentions.remove(existingUsername);
            }
            for (String newMention : newMentions) {
                Optional<User> mentionedOpt = userRepository.findByUsername(newMention);
                if (mentionedOpt.isPresent()) {
                    User mentioned = mentionedOpt.get();
                    notificationService.createNotification(post.getUser().getUsername() +
                            " mentioned you in a post", "post:" + post.getId(), mentioned,
                            notificationTypeService.findByName("postMention"));

                    mentions.add(new PostMention(mentioned, post));
                }
            }
        }

        if (existingFileUrl != null && !existingFileUrl.isEmpty()) {
            s3StorageService.deleteFileFromS3Bucket(existingFileUrl);
            post.setS3Url(null);
        }
        if (newMessage != null && !newMessage.isEmpty()) {
            post.setMessage(newMessage);
        }

        if (newMediaType != null && !newMediaType.isEmpty()) {
            post.setMediaType(newMediaType);
        }

        if (fileUrl != null && !fileUrl.isEmpty()) {
            post.setS3Url(fileUrl);
        }

        post.getTags().clear();

        Set<Tag> newTagsSet = new HashSet<>();
        if (newTagsArray != null) {
            for (String tagName : newTagsArray) {
                Tag tag = tagService.findByName(tagName);
                newTagsSet.add(tag);
            }
        }
        post.setTags(newTagsSet);

        post.setEditTime(new Date());
        postRepository.save(post);
        return postResponseFromPost(post, user, makeFollowedIds(user));
    }

    public void deletePostsByUserId(String userId) {

        List<Post> userPosts = postRepository.findByUserId(userId);

        for (Post post : userPosts) {
            if (post.getS3Url() != null) {
                s3StorageService.deleteFileFromS3Bucket(post.getS3Url());
            }
            postRepository.delete(post);
        }
    }

    public List<PostResponse> getTrending(Date fromDate, String requesterId) {
        List<Post> responses = postRepository.findByCreateTimeGreaterThanEqual(fromDate);
        User requestUser = userService.findById(requesterId);
        Set<String> followedIds = makeFollowedIds(requestUser);

        PriorityQueue<SortedPost> sortedPosts = new PriorityQueue<SortedPost>(11,
                new Comparator<SortedPost>() {
                    @Override
                    public int compare(SortedPost post1, SortedPost post2) {
                        return Double.compare(post1.getScore(), post2.getScore());
                    }
                });

        for (Post userPost : responses) {
            // Integer numberOfVotes = postVoteRepository.findAllByPost(userPost).size();
            SortedPost sortedPost = new SortedPost(postResponseFromPost(userPost, requestUser, followedIds));
            calculateScore(sortedPost);

            sortedPosts.add(sortedPost);
            if (sortedPosts.size() > 10)
                sortedPosts.poll();
        }

        PostResponse[] resPosts = new PostResponse[sortedPosts.size()];
        for (int i = sortedPosts.size() - 1; i >= 0; i--) {
            SortedPost sortedPost = sortedPosts.poll();
            resPosts[i] = sortedPost.getContent();
        }
        return Arrays.asList(resPosts);
    }

    private void calculateScore(SortedPost sortedPost) {
        PostResponse post = sortedPost.getContent();
        int upVotes = post.getUpVotes();
        int downVotes = post.getDownVotes();
        int numberofComments = post.getComments().size();
        int numberofShares = post.getShareCount();
        double score = downVotes + upVotes * 1.5;
        score += (numberofComments * 2);
        score += (numberofShares * 2.5);
        sortedPost.setScore(score);
    }

    private List<String> getMentions(Comment comment) {
        List<String> mentionedUsers = new ArrayList<>();
        Set<CommentMention> mentions = comment.getCommentMentions();
        mentions.forEach(user -> mentionedUsers.add(user.getUser().getUsername()));
        return mentionedUsers;
    }

    private List<String> getMentions(Post post) {
        List<String> mentionedUsers = new ArrayList<>();
        Set<PostMention> mentions = post.getPostMentions();
        mentions.forEach(user -> mentionedUsers.add(user.getUser().getUsername()));
        return mentionedUsers;
    }

}
