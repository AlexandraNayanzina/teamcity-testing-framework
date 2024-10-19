package com.example.teamcity.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum ProjectRoles {
    PROJECT_ADMIN("PROJECT_ADMIN");

    private final String roleName;
}
