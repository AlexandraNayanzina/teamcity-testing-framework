package com.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Step extends BaseModel {

    private String id;

    private String name;

    @Builder.Default
    private String type = "simpleRunner";

    private Properties properties;

    @Data
    public static class Properties {
        private List<Property> property;
        private int count;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Property {
        private String name;
        private String value;
    }
}
