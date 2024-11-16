package org.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.ui.pages.LoginPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateBasePage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import com.example.teamcity.ui.pages.admin.ProjectPage;
import org.example.teamcity.BaseTest;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/alexpshe/spring-core-for-qa";

    @Test(description = "User should be able to create a project", groups = {"Positive"})
    public void userCreatesProject() {

        // GIVEN: preparation of env
        loginAs(testData.getUser());

        // Interaction with UI
        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        // Check the state on API level
        // If data was sent correctly from UI-> API
        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        softy.assertNotNull(createdProject);

        // Check UI
        // Verify that created project is displayed correctly on UI - title,
        ProjectPage.open(createdProject.getId())
                .title.shouldHave(Condition.exactText(testData.getProject().getName()));

        var projectExists = ProjectsPage.open().getProjects().stream().anyMatch(project -> project.getName().text().equals(testData.getProject().getName()));
        softy.assertTrue(projectExists);
        // WHEN: user creates a project and build type
        step("Open 'Create Project Page' (http://localhost:8111/admin/createObjectMenu.html)");
        step("Send al project parameters (repository url, )");
        step("Click Proceed");
        step("Save Project name and Build Type name ");
        step("Click Proceed");

        //THEN
        // Verifying API state
        // (If data was sent correctly c UI to API)
        step("Verify that Project is visible on the Projects page (http://localhost:8111/favorite/projects)");

        // Verifying UI state
        // (If data was read correctly and displayed on UI)
        step("Verify that all entities (project, build type) were created successfully with correct data on API level");

    }


    @Test(description = "User should NOT be able to create a project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {

        // GIVEN: preparation of env
        step("Login as user");
        step("Save the amount of projects");

        // WHEN: user creates a project and build type
        step("Open 'Create Project Page' (http://localhost:8111/admin/createObjectMenu.html)");
        step("Send al project parameters (repository url, )");
        step("Click Proceed");
        step("Set empty project name");
        step("Click Proceed");

        // THEN:

        // Verifying API state
        // (If data was sent correctly c UI to API)
        step("Verify that the Project with empty name was not created");
        step("Verify the amount of  projects was not changed");

        // Verifying UI state
        // (If data was read correctly and displayed on UI)
        step("Verify that the error is displayed 'Project name should not be empty'");

    }
}
