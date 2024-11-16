package com.example.teamcity.api.enums;

import com.example.teamcity.api.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum Endpoint {
    BUILD_TYPES("/app/rest/buildTypes", BuildType.class),
    BUILD_QUEUE("/app/rest/buildQueue", Build.class),
    BUILDS("/app/rest/builds", Build.class),
    PROJECTS("/app/rest/projects", Project.class),
    USERS("/app/rest/users", User.class),
    ROLES("/app/rest/roles", Role.class );


    private final String url;
    private final Class<? extends BaseModel> modelClass;
}
