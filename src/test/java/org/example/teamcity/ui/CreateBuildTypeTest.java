package org.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.admin.BuildTypePage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

@Test(groups = {"Regression"})
public class CreateBuildTypeTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/alexpshe/spring-core-for-qa";
    private static final String REPO_URL_SECOND_BUILD = "https://github.com/AlexandraNayanzina/teamcity-testing-framework";

    @Test(description = "User should be able to create a build type", groups = {"Positive"})
    public void userCreatesBuildType() {
        // GIVEN: preparation of env
        loginAs(testData.getUser());
        var buildType = generate(BuildType.class);

        // API-> Create a project
        var userAuth = Specifications.authSpec(testData.getUser());
        var project = testData.getProject();
        var requests = new CheckedRequests(userAuth);
        requests.getRequest(PROJECTS).create(project);

        // API -> Check the project was created
        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        softy.assertNotNull(createdProject);

        // UI -> create a build type
        CreateBuildTypePage.open(createdProject.getId())
                        .createForm(REPO_URL_SECOND_BUILD)
                        .setupBuildType(buildType.getName());

        // API -> check the build type was created
        var createdBuildType = superUserCheckRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read("name:" + buildType.getName());
        softy.assertNotNull(createdBuildType);

        // UI -> Verify the build type is displayed correctly on UI - by name
        // build type page url http://localhost:8111/buildConfiguration/{build_id}?mode=branches
        BuildTypePage.open(createdBuildType.getId())
                .title.shouldHave(Condition.exactText(buildType.getName()));

       // Tearing down (post-condition) - clearing test data after test
        TestDataStorage.getInstance().addCreatedEntity(PROJECTS, createdProject);

    }


    @Test(description = "User should NOT be able to create a build type without name", groups = {"Negative"})
    public void errorMessageWhenUserCreatesBuildTypeWithEmptyName() {
        // GIVEN: preparation of env
        loginAs(testData.getUser());
        var buildType = generate(BuildType.class);

        // API-> Create a project
        var userAuth = Specifications.authSpec(testData.getUser());
        var project = testData.getProject();
        var requests = new CheckedRequests(userAuth);
        requests.getRequest(PROJECTS).create(project);

        // API -> Check the project was created
        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        softy.assertNotNull(createdProject);

        // UI -> Open build type creation page and try to create a build type with empty name
        CreateBuildTypePage.open(createdProject.getId())
                .createForm(REPO_URL_SECOND_BUILD)
                .errorMessageEmptyBuildTypeName("");

        // API -> check the build type was NOT created
        var response = superUserUncheckRequests.getRequest(BUILD_TYPES).read(buildType.getId())
                .then()
                .assertThat()
                .statusCode(SC_NOT_FOUND);

        TestDataStorage.getInstance().addCreatedEntity(PROJECTS, createdProject);
    }
}
