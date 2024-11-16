package org.example.teamcity.ui;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.admin.CreateBuildStepPage;
import com.example.teamcity.ui.pages.buildConfiguration.BuildTypeStatusPage;
import org.awaitility.Awaitility;
import org.example.teamcity.BuildSteps;
import org.testng.annotations.Test;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import static com.example.teamcity.api.enums.Endpoint.*;


@Test(groups = {"Regression"})
public class RunBuildTypeTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/alexpshe/spring-core-for-qa";

    @Test(description = "User should be able to run build type", groups = {"Positive"})
    public void userRunsBuildType() {

        // GIVEN: preparation of env
        var createdUser = testData.getUser();
        loginAs(createdUser);

        // API-> Create a project
        var userAuth = Specifications.authSpec(createdUser);
        var project = testData.getProject();
        var requests = new CheckedRequests(userAuth);
        requests.getRequest(PROJECTS).create(project);

        // API -> Check the project was created
        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + project.getName());
        softy.assertNotNull(createdProject);

        // API -> Create a build type
        // Setting up Property
        var useCustomScriptProperty = new Step.Property("use.custom.script", "false");
        var commandExetutableProperty = new Step.Property("command.executable", "echo");
        var commandParametersProperty = new Step.Property("command.parameters", "Hello world!");

        // properties
        var properties = new Step.Properties();
        properties.setProperty(List.of(useCustomScriptProperty, commandExetutableProperty, commandParametersProperty));
        properties.setCount(3);

        // step
        var step = Step.builder()
                 .name("Print Hello world")
                .properties(properties)
                .build();

        // steps
        var steps = Steps.builder()
                .count(1)
                .step(List.of(step))
                .build();

        var buildType = testData.getBuildType();
        buildType.setSteps(steps);
        requests.getRequest(BUILD_TYPES).create(buildType);

        // API -> Check the build type was created
        var createdBuildType = superUserCheckRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read("name:" + buildType.getName());
        softy.assertNotNull(createdBuildType);

        // UI -> Create a build step
        CreateBuildStepPage.open(buildType.getId())
                .createBuildStep("test_build_step");

        // API -> Run and wait when build is finished successfully
        var checkedBuildQueueRequest = new CheckedBase<Build>(Specifications.getSpec()
                .authSpec(createdUser), BUILD_QUEUE);
        var build = checkedBuildQueueRequest.create(Build.builder()
                .buildType(testData.getBuildType())
                .build());
        var buildStep = new BuildSteps();
        build = buildStep.waitUntilBuildIsFinished(build, createdUser);
        softy.assertEquals(build.getStatus(), "SUCCESS", "The build Status is not correct");

        // UI -> Verify that build was run successfully on the BuildTypeStatus page
        BuildTypeStatusPage.open(buildType.getId())
                        .verifyBuildTypeRunStatus();

        // Teardown method - Delete the project
        TestDataStorage.getInstance().addCreatedEntity(PROJECTS, createdProject);

    }

}