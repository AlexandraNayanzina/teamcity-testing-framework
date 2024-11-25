package org.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import com.example.teamcity.ui.pages.admin.ProjectPage;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/alexpshe/spring-core-for-qa";

    @Test(description = "User should be able to create a project", groups = {"Positive"})
    public void userCreatesProject() {

        // GIVEN: preparation of env
        loginAs(testData.getUser());

        step("Opening create project page (UI), fill out mandatory fields and submit the form");
        // Interaction with UI
        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        step("Verify the project was created on API level");
        // Check the state on API level
        // If data was sent correctly from UI-> API
        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        softy.assertNotNull(createdProject);

        step("Verify the project is displayed correctly on UI");
        // Check UI
        // Verify that created project is displayed correctly on UI - title,
        ProjectPage.open(createdProject.getId())
                .title.shouldHave(Condition.exactText(testData.getProject().getName()));

        var projectExists = ProjectsPage.open().getProjects().stream().anyMatch(project -> project.getName().text().equals(testData.getProject().getName()));
        softy.assertTrue(projectExists);
    }
}
