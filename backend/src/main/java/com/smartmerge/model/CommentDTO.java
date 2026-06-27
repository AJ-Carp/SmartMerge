package com.smartmerge.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// jackson will ignore null fields. needed because @Builder sets comments to null which github dosnt like
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {

    String body;
    String event;
    List<Comments> comments;


    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Comments {
        String path;
        int position;
        String body;
    }
}

// comments example:
// comments":[
//      {"path": "smart-merge-test/README.md",
//      "position": 5,
//      "body": "another comment test at a specific location"}
// ]