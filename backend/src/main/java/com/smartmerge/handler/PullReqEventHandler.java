package com.smartmerge.handler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.smartmerge.model.PullRequest;
import com.smartmerge.model.Status;
import com.smartmerge.service.InstallationService;
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
    private final InstallationService installationService;

    @Override
    public void triggerEvent(Map<String, Object> webhookPayload, String action) {

        try {
            Map<String, Object> pullRequestData = (Map<String, Object>) webhookPayload.get("pull_request");
            Map<String, Object> installationData = (Map<String, Object>) webhookPayload.get("installation");
            Map<String, Object> repositoryData = (Map<String, Object>) webhookPayload.get("repository");
            Map<String, Object> ownerData = (Map<String, Object>) repositoryData.get("owner");
            Map<String, Object> userData = (Map<String, Object>) pullRequestData.get("user");

            if (action.equals("opened")) {

                // create and save PR to DB
                PullRequest pullRequest = createPullRequest(pullRequestData, installationData, repositoryData, ownerData, userData);
                pullRequestService.savePullRequest(pullRequest);

                // get access token
                long installationId = (long) installationData.get("id");
                String url = installationService.getInstallation(installationId).getAccessTokenUrl();
                String accessToken = tokenService.getInstallationToken(url);

                // collect data needed for service methods
                long pullNumber = (long) pullRequestData.get("number");
                String repoOwner = (String) ownerData.get("login");
                String repoName = (String) repositoryData.get("name");
                long issueNumber = (long) webhookPayload.get("number");

                // fetch data regarding every changed/added/deleted file in the PR
                List<Map<String, Object>> fileData = prFilesService.getFiles(accessToken, repoOwner, repoName, pullNumber);
                List<String> patches = prFilesService.extractPatches(fileData);
                List<String> filesContents = prFilesService.extractFileContents(fileData, accessToken);
                
                // package content for AI review. (name, content, patch) in the subarrays respectivly
                List<String[]> packagedFileContent = prFilesService.packageForReview(fileData, filesContents, patches);    

                // send changes to open AI for review
                String userPrompt = openAIService.buildUserPrompt(packagedFileContent);
                String response = openAIService.prompt(userPrompt);
                String mainReview = openAIService.parseMainReview(response);
                List<String[]> inlineComments = openAIService.parseInlineComments(response);

                // send review to postReview to post it
                reviewService.postReview(accessToken, repoOwner, repoName, issueNumber, mainReview, inlineComments);

                pullRequest.setReviewedAt(OffsetDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS));
                pullRequestService.savePullRequest(pullRequest);

            } else if (action.equals("closed")) {

                long id = (long) pullRequestData.get("id");
                boolean merged = (boolean) pullRequestData.get("merged");
                String closedAt = (String) pullRequestData.get("closed_at");

                PullRequest pullRequest = pullRequestService.getPullRequest(id);
                pullRequest.setClosedAt(OffsetDateTime.parse(closedAt));
                if (merged) {
                    pullRequest.setStatus(Status.MERGED);
                } else {
                    pullRequest.setStatus(Status.CLOSED);
                }
                pullRequestService.savePullRequest(pullRequest);
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
        return PullRequest.builder()
            .id((long) pullRequestData.get("id"))
            .title((String) pullRequestData.get("title"))
            .repoOwnerId((long) ownerData.get("id"))
            .authorId((long) userData.get("id"))
            .authorName((String) userData.get("login"))
            .repoId((long) repositoryData.get("id"))
            .installationId((long) installationData.get("id"))
            .status(Status.OPEN)
            // Github returns UTC 
            .openedAt(OffsetDateTime.parse((String) pullRequestData.get("created_at")))
            .url((String) pullRequestData.get("url"))
            .build();
    }
}
