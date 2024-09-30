package org.example.teamcity.api;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;
import java.util.concurrent.atomic.AtomicReference;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {

    @Test(description = "User should be able to create a build", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        var user = generate(User.class);

        step("Create user", () -> {
            var requester = new CheckedBase<User>(Specifications.superUserSpec(), Endpoint.USERS);
            requester.create(user);
        });

        var project = generate(Project.class);
        AtomicReference<String> projectId = new AtomicReference<>("");

        step("Create project by user", () -> {
            var requester = new CheckedBase<Project>(Specifications.authSpec(user), Endpoint.PROJECTS);
            projectId.set(requester.create(project).getId());
        });

        var buildType = generate(BuildType.class);
        buildType.setProject(Project.builder().id(projectId.get()).locator(null).build());

        var requester = new CheckedBase<BuildType>(Specifications.authSpec(user), Endpoint.BUILD_TYPES);
        AtomicReference<String> buildTypeId = new AtomicReference<>("");

        step("Create buildType for project by user", () -> {
            buildTypeId.set(requester.create(buildType).getId());
        });
        step("Check buildType was created successfully with correct data", () -> {
            var createdBuildType = requester.read(buildTypeId.get());
            softy.assertEquals(buildType.getName(), createdBuildType.getName(), "Build Type name is not correct");
        });
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
