package org.example.teamcity.api;

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.containsString;

@Test(groups = {"Regression", "POST", "project"})
public class ProjectTest extends BaseApiTest {

    @Test(description = "Test Case: Successful project creation", groups = {"Positive"})
    public void projectCreatedSuccessfullyTest() {
        // Given: valid project
        var project = testData.getProject();

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK
        response.then().statusCode(SC_OK);
    }

    @Test(description = "Test Case: Bad request when project is created with missing Name field", groups = {"Negative"})
    public void missingNameFieldTest() { // TODO rename
        // Given: project with null name
        var project = testData.getProject();
        project.setName(null);

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: BAD_REQUEST
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body(containsString("Project name cannot be empty"));
    }

    @Test(description = "Test Case: Missing ID Field", groups = {"Positive", "CRUD"})
    public void t3() { // TODO rename
        // Given: project with null id
        var project = testData.getProject();
        project.setId(null);

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK
        response.then().statusCode(SC_OK);
    }

    @Test(description = "Test Case: Invalid Parent Project Locator", groups = {"Positive", "CRUD"})
    public void t4() { // TODO rename
        // Given: project with invalid Project Locator
        var project = testData.getProject();
        project.setLocator("invalid project locator");

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK - despite invalid project locator
        response.then().statusCode(SC_OK);
    }

    @Test(description = "Test Case: Duplicate Project Id", groups = {"Positive", "CRUD"})
    public void t5() { // TODO rename
        // Given: project 1
        var project1 = testData.getProject();
        superUserCheckRequests.getRequest(PROJECTS).create(project1);

        // When: create project 2 with the same id as project 1
        var teasData2 = generate();
        var project2 = teasData2.getProject();
        project2.setId(project1.getId());
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project2);

        // Then: assert BAD_REQUEST
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body(containsString("Project ID \"%s\" is already used by another project".formatted(project2.getId())));
    }












































    // Cover by test the project create

    @Test(description = "User should be able to create a project with valid data", groups = {"Positive", "CRUD"})
    public void userCreatesProjectTest() {
        // Given new user
        var user = testData.getUser();
        superUserCheckRequests.getRequest(USERS).create(user);

        // When
        var newProject = testData.getProject();
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(user));
        userCheckRequests.<Project>getRequest(PROJECTS).create(newProject);

        // Then
        var createdProject = userCheckRequests.<Project>getRequest(PROJECTS).read(newProject.getId());
        softy.assertEquals(newProject.getName(), createdProject.getName(), "Project name is not correct");
    }

    @Test(description = "User should NOT be able to create a project with empty name", groups = {"Negative", "CRUD"})
    public void userCreatesProjectWithEmptyNameTest() {
        // Given new user
        var user = testData.getUser();
        superUserCheckRequests.getRequest(USERS).create(user);

        // When
        var newProject = testData.getProject();
        newProject.setName("");

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(newProject)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("Project name cannot be empty."));

    }

}
