package com.smartmerge.util;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.smartmerge.model.CommentDTO;
import static com.smartmerge.SmartMergeConstants.GITHUB_BASE_URL;
import static com.smartmerge.SmartMergeConstants.GITHUB_REQUEST_BODY_TYPE;
import java.util.List;

@Service
public class ReviewService {
    
    public void postReview(String accessToken, String repoOwner, String repoName, int issueNumber) {
        CommentDTO testComment = CommentDTO.builder()
            .body("this is a test review comment")
            .event("COMMENT")
            // .comments(List.of(CommentDTO.Comments.builder()
            //     .path("README.md")
            //     .position(5)
            //     .body("another comment test at a specific location")
            //     .build()
            // ))
            .build();
            
        RestClient.create()
            .post()
            .uri(GITHUB_BASE_URL + "/repos/" + repoOwner + "/" + repoName + "/pulls/" + issueNumber + "/reviews")
            .header("Authorization", "Bearer " + accessToken)
            .header("Accept", GITHUB_REQUEST_BODY_TYPE)
            .body(testComment)
            .retrieve()
            .toBodilessEntity();
    }
}
