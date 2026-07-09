package com.smartmerge.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.smartmerge.model.Repo;
import com.smartmerge.service.RepoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class RepoEventHandler implements BaseEventHandler {

    private final RepoService repoService;
    private final RepoMapper repoMapper;

    @Override
    public void triggerEvent(Map<String, Object> webhookPayload, String action) {
        
        if (action.equals("added")) {
            Map<String, Object> installationData = (Map<String, Object>) webhookPayload.get("installation");
            Map<String, Object> accountData = (Map<String, Object>) installationData.get("account");
            List<Map<String, Object>> repoData = (List<Map<String, Object>>) webhookPayload.get("repositories_added");

            List<Repo> repos = repoMapper.createRepos(repoData, installationData, accountData);
            repoService.saveAllRepos(repos);
        }
        else if (action.equals("removed")) {
            List<Map<String, Object>> repoData = (List<Map<String, Object>>) webhookPayload.get("repositories_removed");

            List<Long> repoIds = getRepoIds(repoData);
            // delete all repos and associated PR's
            repoService.deleteAllRepos(repoIds);
        }
        else {
            log.warn("No implementation for action={}", action);
        }
    }

    private List<Long> getRepoIds(List<Map<String, Object>> repoData) {
        List<Long> repoIds = new ArrayList<>();
        
        for (Map<String, Object> repo : repoData) {
            repoIds.add(((Number) repo.get("id")).longValue());
        }
        return repoIds;
    }
}
