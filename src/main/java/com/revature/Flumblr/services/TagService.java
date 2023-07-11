package com.revature.Flumblr.services;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.revature.Flumblr.entities.Post;
import com.revature.Flumblr.entities.PostVote;
import com.revature.Flumblr.entities.Tag;
import com.revature.Flumblr.repositories.PostRepository;
import com.revature.Flumblr.repositories.TagRepository;
import com.revature.Flumblr.utils.custom_classes.SortedTag;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public Tag findByName(String name) {
        Optional<Tag> tag = tagRepository.findByName(name);
        if (tag.isEmpty()) {
            return createTag(name);
        }
        return tag.get();
    }

    public List<String> getTrending(Date date) {
        Iterable<Post> posts = postRepository.findAllWithTagsAfter(date);
        Map<String, Double> tagMap = new HashMap<String, Double>();
        PriorityQueue<SortedTag> sortedTags = new PriorityQueue<SortedTag>(10,
                new Comparator<SortedTag>() {
                    @Override
                    public int compare(SortedTag post1, SortedTag post2) {
                        return Double.compare(post1.getScore(), post2.getScore());
                    }
                });
        for(Post post : posts) {
            for(Tag tag : post.getTags()) {
                String name = tag.getName();
                double score = calculateScore(post);
                double currentScore = tagMap.getOrDefault(name, 0.0);
                tagMap.put(name, currentScore + score);
            }
        }
        for(Map.Entry<String, Double> entry : tagMap.entrySet()) {
            if(sortedTags.size() >= 10) {
                double minScore = sortedTags.peek().getScore();
                double currentScore = entry.getValue();
                if(minScore < currentScore) {
                    SortedTag popTag = sortedTags.poll();
                    popTag.setScore(currentScore);
                    popTag.setName(entry.getKey());
                    sortedTags.add(popTag);
                }
            } else {
                SortedTag sortedTag = new SortedTag(entry.getKey(), entry.getValue());
                sortedTags.add(sortedTag);
            }
        }
        String[] resNames = new String[sortedTags.size()];
        for (int i = sortedTags.size() - 1; i >= 0; i--) {
            SortedTag sortedTag = sortedTags.poll();
            resNames[i] = sortedTag.getName();
        }
        return Arrays.asList(resNames);
    }

    private double calculateScore(Post post) {
        int upVotes = 0;
        int downVotes;
        Set<PostVote> postVotes = post.getPostVotes();
        for (PostVote postVote : postVotes) {
            if (postVote.isVote())
                upVotes++;
        }
        downVotes = postVotes.size() - upVotes;
        int numberofComments = post.getComments().size();
        int numberofShares = post.getPostShares().size();
        double score = downVotes + upVotes * 1.5;
        score += (numberofComments * 2);
        score += (numberofShares * 2.5);
        return score;
    }

    public Tag createTag(String name) {

        Tag tag = new Tag(name);
        tagRepository.save(tag);
        return tag;
    }
}
