package com.smartmerge.util;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {
    
    private final ChatClient chatClient;
    private final String systemPrompt = "You are a senior engineer. Review these files and look for bugs and stylistic issues. Feel free to make any suggestions. \n" +
                                        "Each file is contained by a main block (seperated by equal signs). Within each main block, the files are seperated into three " + 
                                        "smaller blocks (seperated by hyphens): File Name, File Contents, and File Patch."; 

    public OpenAIService(OpenAiChatModel openAiChatModel) {
        chatClient = ChatClient.create(openAiChatModel);
    }

    public String prompt(String userPrompt) {
        return chatClient.prompt()
            .system(systemPrompt)
            .user(userPrompt)
            .call()
            .content();
    }

    public String buildUserPrompt(List<String[]> fullFileContent) {
        StringBuilder userPrompt = new StringBuilder();

        for (String[] contents : fullFileContent) {
            userPrompt.append("==========================File===========================\n\n");

            userPrompt.append("------------------------File Name------------------------\n");
            userPrompt.append(contents[0] + "\n");
            userPrompt.append("---------------------------------------------------------\n\n");

            userPrompt.append("------------------------File Contents--------------------\n");
            userPrompt.append(contents[1] + "\n");
            userPrompt.append("---------------------------------------------------------\n\n");

            userPrompt.append("-------------------------File Patch----------------------\n");
            userPrompt.append(contents[2] + "\n");
            userPrompt.append("---------------------------------------------------------\n\n");

            userPrompt.append("=========================================================\n\n\n");
        }
        return userPrompt.toString();
    }
}