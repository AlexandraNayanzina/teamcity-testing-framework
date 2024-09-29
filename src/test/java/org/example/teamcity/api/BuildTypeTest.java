package org.example.teamcity.api;

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {

    @Test
    public void buildConfigurationTest() {
        var user =  User.builder()
                .username("admin")
                .password("admin")
                .build();

        var token = RestAssured
                .given()
                .spec(Specifications.getSpec().authSpec(user))
                .get("/authenticationTest.html?csrf")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().asString();

        System.out.println(token);

    }
    @Test(description = "User should be able to create a build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        step("Create user");
        step("Create project by user");
        step("Create buildType for project by user");
        step("Check buildType was created successfully with correct data");
    }

    @Test(description = "User should NOT be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        step("Create user");
        step("Create project by user");
        step("Create buildType1 for project by user");
        step("Create buildType2 with the same id as buildType1 for project by user");
        step("Check buildType2 was NOT created with bad request status code");
    }

    @Test(description = "Project Admin should be able to create two build types for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest() {
        step("Create user");
        step("Create project");
        step("Grand PROJECT_ADMIN role to the user");

        step("Create buildType1 for project by PROJECT_ADMIN user");
        step("Check buildType was created successfully");
    }

    @Test(description = "Project Admin should NOT be able to create buildType for another user project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        step("Create user1");
        step("Create project1");
        step("Grand PROJECT_ADMIN role to the user1");

        step("Create user2");
        step("Create project2");
        step("Grand PROJECT_ADMIN role to the user2");

        step("Create buildType1 for project1 by user2");
        step("Check buildType was NOT created with forbidden status code");
    }
}
