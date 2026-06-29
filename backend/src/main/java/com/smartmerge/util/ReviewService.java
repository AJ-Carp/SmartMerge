package com.smartmerge.util;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.smartmerge.model.CommentDTO;
import static com.smartmerge.SmartMergeConstants.GITHUB_BASE_URL;
import static com.smartmerge.SmartMergeConstants.GITHUB_REQUEST_BODY_TYPE;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    
    public void postReview(String accessToken, String repoOwner, String repoName, int issueNumber, String mainReview, List<String[]> inlineComments) {
        List<CommentDTO.Comments> comments = generateComments(inlineComments);
        CommentDTO Review = CommentDTO.builder()
            .body(mainReview)
            .event("COMMENT")
            .comments(comments)
            .build();
            
        RestClient.create()
            .post()
            .uri(GITHUB_BASE_URL + "/repos/" + repoOwner + "/" + repoName + "/pulls/" + issueNumber + "/reviews")
            .header("Authorization", "Bearer " + accessToken)
            .header("Accept", GITHUB_REQUEST_BODY_TYPE)
            .body(Review)
            .retrieve()
            .toBodilessEntity();
    }

    private List<CommentDTO.Comments> generateComments(List<String[]> inlineComments) {
        List<CommentDTO.Comments> comments = new ArrayList<>();
        for (String[] commentArray : inlineComments) {
            comments.add(CommentDTO.Comments.builder()
                .path(commentArray[0])
                .position(Integer.parseInt(commentArray[1]))
                .body(commentArray[2])
                .build());
        }
        return comments;
    }
}
