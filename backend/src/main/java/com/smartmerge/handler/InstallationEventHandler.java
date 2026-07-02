package com.smartmerge.handler;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.smartmerge.model.Installation;
import com.smartmerge.model.Repo;
import com.smartmerge.service.InstallationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// created (installed) - add installation and add repos
// deleted (uninstalled) - remove installation and remove repos

@Slf4j
@RequiredArgsConstructor
@Component
public class InstallationEventHandler implements BaseEventHandler {

    private final InstallationService installationService;
    private final RepoMapper repoMapper;

    @Override
    public void triggerEvent(Map<String, Object> webhookPayload, String action) {
        try {
            Map<String, Object> installationData = (Map<String, Object>) webhookPayload.get("installation");
            Map<String, Object> accountData = (Map<String, Object>) installationData.get("account");
            List<Map<String, Object>> repoData = (List<Map<String, Object>>) webhookPayload.get("repositories");

            if (action.equals("created")) {
                // create installations and repos
                Installation installation = createInstallation(installationData, accountData);
                List<Repo> repos = repoMapper.createRepos(repoData, installationData, accountData);

                // save installations and repos to DB
                installationService.saveInstallationAndRepos(installation, repos);
            } 
            else if (action.equals("deleted")) {
                long installationId = (long) installationData.get("id");

                // deletes installation and all associated repos and PRs atomically
                installationService.deleteInstallation(installationId);
            } 
            else {
                log.info("No implementation for action={}", action);
            }
        } 
        catch (Exception e) {
            log.error("Action={}", action, e);
        }
    }

    private Installation createInstallation(Map<String, Object> installationData, Map<String, Object> accountData) {
        Installation installation = Installation.builder()
            .installationId((long) installationData.get("id"))
            .userId((long) accountData.get("id"))
            .accessTokenUrl((String) installationData.get("access_tokens_url")).build();

        return installation;
    }
}
