package com.smartmerge.handler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.smartmerge.model.PullRequest;
import com.smartmerge.model.Status;
import com.smartmerge.service.PullRequestService;
import com.smartmerge.util.ReviewService;
import com.smartmerge.util.TokenService;
import com.smartmerge.util.OpenAIService;
import com.smartmerge.util.PrFilesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class PullReqEventHandler implements BaseEventHandler {

    private final PullRequestService pullRequestService;
    private final TokenService tokenService;
    private final ReviewService reviewService;
    private final PrFilesService prFilesService;
    private final OpenAIService openAIService;

    @Override
    public void triggerEvent(Map<String, Object> webhookPayload, String action) {
        try {
            Map<String, Object> pullRequestData = (Map<String, Object>)webhookPayload.get("pull_request");
            Map<String, Object> installationData = (Map<String, Object>)webhookPayload.get("installation");
            Map<String, Object> repositoryData = (Map<String, Object>)webhookPayload.get("repository");
            Map<String, Object> ownerData = (Map<String, Object>)repositoryData.get("owner");
            Map<String, Object> userData = (Map<String, Object>)pullRequestData.get("user");
            if (action.equals("opened")) {

                // get access token
                int installationId = (int)installationData.get("id");
                String url = "https://api.github.com/app/installations/" + installationId + "/access_tokens"; // could retrieve url from installation instead
                String accessToken = tokenService.getInstallationToken(url);

                // collect data needed for service methods
                int pullNumber = (int)pullRequestData.get("number");
                String repoOwner = (String)ownerData.get("login");
                String repoName = (String)repositoryData.get("name");
                int issueNumber = (int)webhookPayload.get("number");

                // fetch data regarding every changed/added/deleted file in the PR
                List<Map<String, Object>> fileData = prFilesService.getFiles(accessToken, repoOwner, repoName, pullNumber);
                List<String> patches = prFilesService.extractPatches(fileData);
                for (String patch : patches) {
                    System.out.println(patch + "\n \n");
                }
                List<String> filesContents = prFilesService.extractFileContents(fileData, accessToken);
                System.out.println(filesContents);
                

                // send chnages to open AI for review (new file for this)
                

                // send review to postReview to post it
                reviewService.postReview(accessToken, repoOwner, repoName, issueNumber);

                // create and save PR to DB
                PullRequest pullRequest = createPullRequest(pullRequestData, installationData, repositoryData, ownerData, userData);
                pullRequestService.savePullRequest(pullRequest);

            } else if (action.equals("closed")) {

            } else {
                log.info("No implementation for action={}", action);
            }
        } catch (Exception e) {
            log.error("Action={}", action, e);
        }
    }

    private PullRequest createPullRequest(
        Map<String, Object> pullRequestData,
        Map<String, Object> installationData,
        Map<String, Object> repositoryData,
        Map<String, Object> ownerData,
        Map<String, Object> userData
    ) {
        // System.out.println(pullRequestData.get("created_at"));
        // System.out.println(OffsetDateTime.now());
        return PullRequest.builder()
            .id((long)pullRequestData.get("id"))
            .title((String)pullRequestData.get("title"))
            .repoOwnerId((int)ownerData.get("id"))
            .authorId((int)userData.get("id"))
            .authorName((String)userData.get("login"))
            .repoId((int)repositoryData.get("id"))
            .installationId((int)installationData.get("id"))
            .status(Status.OPEN)
            //.openedAt((OffsetDateTime)pullRequestData.get("created_at"))
            .url((String)pullRequestData.get("url"))
            .build();
    }
}
