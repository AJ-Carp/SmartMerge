package com.smartmerge.util;

import static com.smartmerge.SmartMergeConstants.GITHUB_BASE_URL;
import static com.smartmerge.SmartMergeConstants.GITHUB_REQUEST_BODY_TYPE;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PrFilesService {
    
    public List<Map<String, Object>> getFiles(String accessToken, String repoOwner, String repoName, int pullNumber) {
        List<Map<String, Object>> response = RestClient.create()
            .get()
            .uri(GITHUB_BASE_URL + "/repos/" + repoOwner + "/" + repoName + "/pulls/" + pullNumber + "/files")
            .header("Authorization", "Bearer " + accessToken)
            .header("Accept", GITHUB_REQUEST_BODY_TYPE)
            .retrieve()
            .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});

        // making json repsonse legible for development purposes
        ObjectMapper mapper = new ObjectMapper();
        try {
            String legibleJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
            System.out.println(legibleJson);
        } catch (Exception e) {
            System.out.println("error processing JSON");
        }

        return response;
    }
}
