package com.smartmerge.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.smartmerge.SmartMergeConstants.GITHUB_WEB_HOOK_ROUTE;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.smartmerge.handler.BaseEventHandler;
import com.smartmerge.handler.InstallationEventHandler;
import com.smartmerge.handler.PullReqEventHandler;
import com.smartmerge.handler.RepoEventHandler;
import jakarta.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(GITHUB_WEB_HOOK_ROUTE)
public class GithubWebhooks {
    
    private final InstallationEventHandler installationEventHandler;
    private final RepoEventHandler repoEventHandler;
    private final PullReqEventHandler pullReqEventHandler;
    private Map<String, BaseEventHandler> eventHandlerMap;
    
    @PostConstruct
    public void buildEventHandlerMap() {
        eventHandlerMap = Map.of(
            "created", installationEventHandler,
            "deleted", installationEventHandler,
            "added", repoEventHandler,
            "removed", repoEventHandler,
            "opened", pullReqEventHandler,
            "closed", pullReqEventHandler
        );
    }

    @PostMapping("/github")
    public void handleEvent(@RequestBody Map<String, Object> payload) {
        String action = (String)payload.get("action");
        BaseEventHandler baseEventHandler = (BaseEventHandler)eventHandlerMap.getOrDefault(action, null);
        if (baseEventHandler != null) {
            baseEventHandler.triggerEvent(payload, action);
        } else {
            log.info("no handler for action={}", action);
        }
    }
}
