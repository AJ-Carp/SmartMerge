package com.smartmerge.util;

import static com.smartmerge.SmartMergeConstants.GITHUB_BASE_URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrFilesService {

    private final GithubServiceCaller githubServiceCaller;
    
    public List<Map<String, Object>> getFiles(String accessToken, String repoOwner, String repoName, long pullNumber) {
        String uri = GITHUB_BASE_URL + "/repos/" + repoOwner + "/" + repoName + "/pulls/" + pullNumber + "/files";
        return githubServiceCaller.get(uri, accessToken, new ParameterizedTypeReference<List<Map<String,Object>>>() {});
    }

    public List<String> extractPatches(List<Map<String, Object>> fileData) {
        List<String> patches = new ArrayList<>();
        for (Map<String, Object> file : fileData) {
            String patch = (String) file.get("patch");
            patches.add(patch);
        }
        return patches;
    }

    public List<String> extractFileContents(List<Map<String, Object>> fileData, String accessToken) {
        List<String> fileContents = new ArrayList<>();
        for (Map<String, Object> data : fileData) {
            String contentsUrl = (String) data.get("contents_url");
            Map<String, Object> response = githubServiceCaller.get(contentsUrl, accessToken, new ParameterizedTypeReference<Map<String, Object>>() {});
            // returns base64
            String encodedContent = (String) response.get("content");
            String decodedContent = new String(java.util.Base64.getMimeDecoder().decode(encodedContent));
            String fileContent = addLineNumbers(decodedContent);
            fileContents.add(fileContent);
        }
        return fileContents;
    }

    // adding line numbers so AI dosnt have to count lines to make inline comments. Improves accuracy
    private String addLineNumbers(String fileContent) {
        StringBuilder numberedFileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(fileContent))) {
            String line;
            for (int i = 1; ((line = reader.readLine()) != null); i++) {
                numberedFileContent.append(i).append(": ").append(line).append("\n");
            }
        } catch (IOException e) {
            log.error("Error adding line numbers to file contents", e);
        }
        return numberedFileContent.toString();
    }

    public List<String[]> packageForReview(List<Map<String, Object>> fileData, List<String> filesContents, List<String> patches) {
        List<String[]> packagedFileContent = new ArrayList<>();
        for (int i = 0; i < fileData.size(); i++) {
            String[] subList = new String[3];
            subList[0] = (String) fileData.get(i).get("filename");
            subList[1] = filesContents.get(i);
            subList[2] = patches.get(i);
            packagedFileContent.add(subList);
        }
        return packagedFileContent;
    }
}
