package com.smartmerge.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OpenAIService {
    
    private final ChatClient chatClient;
    private final String systemPrompt = """
        You are a senior engineer reviewing a pull request. First, write a short summary of what the
        changes do, so the author knows you understood them. Then review the code for bugs and
        stylistic issues, and make any suggestions you think are helpful.

        The input contains one block per file (enclosed by equal signs). Each file block has three
        sub-blocks (enclosed by hyphens): File Path, File Contents, and File Patch. The File Patch is a
        unified diff showing what changed in this PR.

        Every line in the File Contents block is prefixed with its line number, e.g. "12: <code>".
        When you make an inline comment, use that prefixed number for the line — do not count lines
        yourself. Only comment on lines that are visible in the File Patch.

        List any inline comments at the very bottom of your response, with no heading (this section is
        parsed programmatically). Put each comment on a single line, in exactly this format:

        INLINE_COMMENT: <file path> | <line number> | <comment>

        For example:
        INLINE_COMMENT: src/main/java/Counter.java | 3 | Consider a for-loop here because ...
        INLINE_COMMENT: src/main/java/Animal.java | 16 | This variable name is unclear because ...
        """;
                                        
                                    

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

            userPrompt.append("------------------------File Path------------------------\n");
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

    public String parseMainReview(String response) {
        StringBuilder mainReview = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(response))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("INLINE_COMMENT:")) {
                    mainReview.append(line + "\n");
                }
            }
            
        } catch (IOException e) {
            log.error("Error parsing main review", e);
        }
        return mainReview.toString();
    }

    // turn each inline comment that AI decided to make into a list of 3: [filePath, line, comment]
    public List<String[]> parseInlineComments(String response) {
        List<String[]> inlineComments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(response))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("INLINE_COMMENT:")) {
                    line = line.replace("INLINE_COMMENT:", "");
                    String[] sub = new String[3];
                    buildSubArray(line, sub);
                    inlineComments.add(sub);
                }
            }

        } catch (IOException e) {
            log.error("Error parsing main review", e);
        }
        return inlineComments;
    }

    private void buildSubArray(String line, String[] sub) {
        StringBuilder filePath = new StringBuilder();
        StringBuilder position = new StringBuilder();
        StringBuilder comment = new StringBuilder();
        int i = 0;
        char c = line.charAt(i);
        try {
            while (i < line.length() && c != '|') {
                filePath.append(c);
                c = line.charAt(++i);
            }
            c = line.charAt(++i);
            while (i < line.length() && c != '|') {
                position.append(c);
                c = line.charAt(++i);
            }
            c = line.charAt(++i);
            while (i < line.length() - 1) {
                comment.append(c);
                c = line.charAt(++i);
            }
            comment.append(c);
            sub[0] = filePath.toString().trim();
            sub[1] = position.toString().trim();
            sub[2] = comment.toString().trim();
        } catch (Exception e) {
            log.error("Malformed inline comments provided by AI", e);
        }
    }
}