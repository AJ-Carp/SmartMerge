package com.smartmerge.util;

import static com.smartmerge.SmartMergeConstants.GITHUB_BASE_URL;
import static com.smartmerge.SmartMergeConstants.GITHUB_REQUEST_BODY_TYPE;
import java.util.ArrayList;
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

    public List<String> extractPatches(List<Map<String, Object>> fileData) {
        List<String> patches = new ArrayList<>();
        for (Map<String, Object> file : fileData) {
            String patch = (String)file.get("patch");
            patches.add(patch);
        }
        return patches;
    }

    public List<String> extractFileContents(List<Map<String, Object>> fileData, String accessToken) {
        List<String> fileContents = new ArrayList<>();
        for (Map<String, Object> data : fileData) {
            String contentsUrl = (String)data.get("contents_url");
            Map<String, Object> response = RestClient.create()
                .get()
                .uri(contentsUrl)
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", GITHUB_REQUEST_BODY_TYPE)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
            // returns base64
            String encodedContent = (String)response.get("content");
            String decodedContent = new String(java.util.Base64.getMimeDecoder().decode(encodedContent));
            fileContents.add(decodedContent);
        }
        return fileContents;
    }

    public List<String[]> packageForReview(List<String> patches, List<String> filesContents, List<Map<String, Object>> fileData) {
        List<String[]> fullFileContent = new ArrayList<>();
        for (int i = 0; i < fileData.size(); i++) {
            String[] subList = new String[3];
            subList[0] = (String)fileData.get(i).get("filename");
            subList[1] = filesContents.get(i);
            subList[2] = patches.get(i);
            fullFileContent.add(subList);
        }
        return fullFileContent;
    }
}
