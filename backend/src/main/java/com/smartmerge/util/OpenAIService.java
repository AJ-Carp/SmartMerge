package com.smartmerge.util;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {
    
    private final ChatClient chatClient;

    public OpenAIService(OpenAiChatModel openAiChatModel) {
        chatClient = ChatClient.create(openAiChatModel);
    }

    public void prompt() {
        
    }

}
