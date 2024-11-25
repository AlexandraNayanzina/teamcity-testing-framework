package org.example.teamcity.api;

import com.example.teamcity.api.generators.TestDataStorage;
import org.testng.annotations.Test;
import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;

@Test(groups = {"Regression", "CRUD", "project"})
public class ProjectTest extends BaseApiTest {

    @Test(description = "Test Case: Successful project creation", groups = {"Positive"})

    public void projectCreatedSuccessfullyTest() {

        // Given: valid project
        var project = testData.getProject();

        step("Creating a project");
        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        step("Verify the project was successfully created");
        // Assert: OK
        response.then().statusCode(SC_OK);
    }

    @Test(description = "Test Case: Bad request when project is created with null Name field", groups = {"Negative"})
    public void missingNameFieldTest() {
        // Given: project with null name
        var project = testData.getProject();
        project.setName(null);

        step("Creating a project with an name null");
        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        step("Verify the project was not created");
        // Assert: BAD_REQUEST
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body(containsString("Project name cannot be empty"));
    }

    @Test(description = "Test Case: Bad request when project is created with empty Name field", groups = {"Negative"})
    public void emptyNameFieldTest() {
        // Given: project with empty name
        var project = testData.getProject();
        project.setName("");

        step("Creating a project with an empty name");
        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        step("Verify the project was not created");
        // Assert: BAD_REQUEST
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body(containsString("Project name cannot be empty"));
    }

    @Test(description = "Test Case: Project is created if Name is more than 200 characters long", groups = {"Positive"})
    public void longNameFieldTest() {
        // Given: project with name containing more than 200 characters
        var project = testData.getProject();
        var project_name = "Long name more than 100 characters Long name more than 100 characters" +
                "long name more than 100 characters Long name more than 100 characters Long name more than 100 characters " +
                "Long name more than 100 characters" + System.currentTimeMillis();
        project.setName(project_name);

        step("Creating a project with the name containing more than 200 characters");
        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);


        step("Verify the project was created");
        // Assert: OK
        response.then()
                .statusCode(SC_OK);
    }

    @Test(description = "Test Case: Project is created if Name field contains special character", groups = {"Positive"})
    public void specialCharacterNameFieldTest() {
        // Given: project with name containing a special character and one special char
        var project = testData.getProject();
        project.setName(project.getName() + "!test");

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK
        response.then()
                .statusCode(SC_OK);
    }

    @Test(description = "Test Case: Project is created if Name field contains only special character", groups = {"Positive"})
    public void onlySpecialCharacterNameFieldTest() {
        // Given: project with name containing only one special character
        var project = testData.getProject();
        project.setName(project.getName() + "!");

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK
        response.then()
                .statusCode(SC_OK);
    }

    @Test(description = "Test Case: Project is created if Name field contains space character", groups = {"Positive"})
    public void spaceCharacterNameFieldTest() {
        // Given: project with name containing space character
        var project = testData.getProject();
        project.setName(project.getName() + " ");

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK
        response.then()
                .statusCode(SC_OK);
    }

    @Test(description = "Test Case: Project is created when ID field is missing", groups = {"Positive"})
    public void missingIdFieldTest() {
        // Given: project with null id field
        var project = testData.getProject();
        project.setId(null);

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK
        response.then().statusCode(SC_OK);
    }

    @Test(description = "Test Case: Project is created when ID field is empty", groups = {"Positive"})
    public void emptyIdFieldTest() {
        // Given: project with empty id field
        var project = testData.getProject();
        project.setId(null);

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK
        response.then().statusCode(SC_OK);
    }

    @Test(description = "Test Case: Internal Server Error when ID field contains a special character", groups = {"Negative"})
    public void specialCharacterIdFieldTest() {
        // Given: project with id field containing a special character
        var project = testData.getProject();
        var special_char = "!";
        project.setId(special_char + "test");

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: INTERNAL_SERVER_ERROR
        response.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .body(containsString("ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters)."));
    }

    @Test(description = "Test Case: Internal Server Error when ID field contains a space character", groups = {"Positive"})
    public void spaceCharacterIdFieldTest() {
        // Given: project with id field containing a space character
        var project = testData.getProject();
        var special_char = " ";
        project.setId("test" + special_char);

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: INTERNAL_SERVER_ERROR
        response.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .body(containsString("ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters)."));
    }

    @Test(description = "Test Case: Internal Server Error when ID field contains a cyrillic character", groups = {"Negative"})
    public void cyrillicCharacterIdFieldTest() {
        // Given: project with id field containing a cyrillic character
        var project = testData.getProject();
        var cyrillic_char = "ж";
        project.setId(cyrillic_char + "test");

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: INTERNAL_SERVER_ERROR
        response.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .body(containsString("ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters)."));
    }

    @Test(description = "Test Case: Project is created successfully when Locator field is a random String", groups = {"Positive"})
    public void randomStringLocatorFieldTest() {
        // Given: project with id field containing a random string
        var project = testData.getProject();
        project.setLocator("test");

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK
        response.then()
                .statusCode(SC_OK);
    }

    @Test(description = "Test Case: Project is created successfully when Locator field contains a special character", groups = {"Positive"})
    public void specialCharLocatorFieldTest() {
        // Given: project with id field containing a special character
        var project = testData.getProject();
        var special_char = "!";
        project.setLocator(special_char + "test");

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK
        response.then()
                .statusCode(SC_OK);
    }

    @Test(description = "Test Case: Project is created successfully when Locator field is NOT null AND ID field is null", groups = {"Positive"})
    public void nullIdLocatorFieldsTest() {
        // Given: project with id field is null and locator is not null
        var project = testData.getProject();
        project.setId(null);
        project.setLocator("test");

        // When: create project
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project);

        // Assert: OK
        response.then()
                .statusCode(SC_OK);
    }

    @Test(description = "Test Case: Project is NOT created with the same Id as another project", groups = {"Negative"})
    public void duplicatedIdTest() {
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

    @Test(description = "Test Case: Project is NOT created with the same name as another project", groups = {"Negative"})
    public void duplicatedNameTest() {
        // Given: project 1
        var project1 = testData.getProject();
        superUserCheckRequests.getRequest(PROJECTS).create(project1);

        // When: create project 2 with the same Name as project 1
        var teasData2 = generate();
        var project2 = teasData2.getProject();
        project2.setName(project1.getName());
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project2);

        // Then: assert BAD_REQUEST
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body(containsString("Project with this name already exists:"));
    }

    @Test(description = "Test Case: Project is created with the same locator as another project", groups = {"Positive"})
    public void duplicatedLocatorTest() {
        // Given: project 1
        var project1 = testData.getProject();
        project1.setLocator("test");
        superUserCheckRequests.getRequest(PROJECTS).create(project1);

        // When: create project 2 with the same locator as project 1
        var teasData2 = generate();
        var project2 = teasData2.getProject();
        project2.setLocator(project1.getLocator());
        var response = superUserUncheckRequests.getRequest(PROJECTS).create(project2);

        // Then: assert OK
        response.then()
                .statusCode(SC_OK);
    }


    @Test(description = "Test Case: user can delete the project", groups = {"Positive"})
    public void deleteProjectTest() {
        // Given: user creates a project
        var project = testData.getProject();
        var id = project.getId();
        superUserCheckRequests.getRequest(PROJECTS).create(project);

        // When: user deletes the project
       superUserUncheckRequests.getRequest(PROJECTS).delete(project.getId());

        // Then: assert SC_NOT_FOUND when try to get the project by id
        superUserUncheckRequests.getRequest(PROJECTS).read(id)
                .then()
                .assertThat()
                .statusCode(SC_NOT_FOUND);

        TestDataStorage.getInstance().removeFromTheMap(PROJECTS, id);
    }

}
