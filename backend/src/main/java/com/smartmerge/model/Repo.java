package com.smartmerge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Repo {

    @Id
    private int repoId;

    private int userId;

    private int installationId;

    private String repoName;

    private boolean isPrivate;

}
