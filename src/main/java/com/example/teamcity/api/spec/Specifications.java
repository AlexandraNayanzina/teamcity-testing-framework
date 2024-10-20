package com.example.teamcity.api.spec;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.Matchers.containsString;

public class Specifications {

    /* Class Specification realises a design pattern - Singleton (restricts the instantiation of a class to a singular instance)
    1. Create empty Constructor
    2. Create method that returns the spec
    private Specifications(){}

    public static Specifications getSpec() {
    if (spec == null) {
    spec = new Specifications();}
    return spec;
    }
     */

    private static Specifications spec;


    private static RequestSpecBuilder  reqBuilder(){
        var requestBuilder = new RequestSpecBuilder();
        requestBuilder.addFilter(new RequestLoggingFilter());
        requestBuilder.addFilter(new ResponseLoggingFilter());
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.setAccept(ContentType.JSON);
        return requestBuilder;
    }

    public static RequestSpecification superUserSpec() {
        var requestBuilder = reqBuilder();
        requestBuilder.setBaseUri("http://%s:%s@%s".formatted("",Config.getProperty("superUserToken"), Config.getProperty("host")));
        return requestBuilder.build();
    }

    public static RequestSpecification unauthSpec() {
        // Request for unauth user
        var requestBuilder = reqBuilder();
        return requestBuilder.build();
    }

    public static RequestSpecification authSpec(User user) {
        var requestBuilder = reqBuilder();
        requestBuilder.setBaseUri("http://%s:%s@%s".formatted(user.getUsername(), user.getPassword(), Config.getProperty("host")));
        return requestBuilder.build();
    }

    public static void assertStatusCodeAndBody(Response response, int statusCode,String responseBody) {
        response.then()
                .assertThat()
                .statusCode(statusCode)
                .body(containsString(responseBody));
    }
}
