package org.example.teamcity.api;

import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;
import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.enums.ProjectRoles.PROJECT_ADMIN;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static com.example.teamcity.api.spec.Specifications.assertStatusCodeAndBody;
import static org.apache.http.HttpStatus.*;

@Test(groups = {"Regression"})
public class BuildTypeTestHomeWork extends BaseApiTest {

    @Test(description = "User should be able to create a build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {

        // GIVEN
        // SuperUser creates a user
        var user = testData.getUser();
        superUserCheckRequests.getRequest(USERS).create(user);
        var userAuth = Specifications.authSpec(user);

        // the user creates a project
        var project = testData.getProject();
        var requests = new CheckedRequests(userAuth);
        requests.getRequest(PROJECTS).create(project);

        // WHEN
        // the user creates a build type
        var buildType = testData.getBuildType();
        requests.getRequest(BUILD_TYPES).create(buildType);

        // THEN
        // assert the build type can be retrieved by id
        var createdBuildType = requests.<BuildType>getRequest(BUILD_TYPES).read("id:" + buildType.getId());
        softy.assertEquals(buildType.getName(), createdBuildType.getName(), "Build Type name is not correct");
        softy.assertEquals(buildType.getProject(), createdBuildType.getProject(), "Build Type project is not correct");
    }

    @Test(description = "User should NOT be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        // GIVEN
        // sureUser creates a user
        var user = testData.getUser();
        superUserCheckRequests.getRequest(USERS).create(user);

        // create user requester
        var userAuth = Specifications.authSpec(user);
        var requester = new CheckedRequests(userAuth);

        // the user creates a project
        var project = testData.getProject();
        requester.getRequest(PROJECTS).create(project);

        // the user creates first buildType for the project
        var firstBuildType = testData.getBuildType();
        requester.getRequest(BUILD_TYPES).create(firstBuildType);

        // WHEN: the user creates second buildType
        var secondTestData = generate();
        var secondBuildType = secondTestData.getBuildType();
        secondBuildType.setId(firstBuildType.getId());
        secondBuildType.setProject(project);
        var requesterUnchecked = new UncheckedBase(userAuth, BUILD_TYPES);
        var response = requesterUnchecked.create(secondBuildType);

        // THEN assert: BAD_REQUEST
        assertStatusCodeAndBody(response, SC_BAD_REQUEST, "The build configuration / template ID \"%s\" is already used by another configuration or template".formatted(testData.getBuildType().getId()));

    }

    @Test(description = "Project Admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest() {
        // GIVEN
        // SuperUser creates a project
        var project = testData.getProject();
        superUserCheckRequests.getRequest(PROJECTS).create(project);

        // SuperUser creates an ProjectAdmin user assigned to the project
        var projectAdmin = testData.getUser();
        var roles = generate(Roles.class, PROJECT_ADMIN.getRoleName(), "p:" + testData.getProject().getId());
        projectAdmin.setRoles(roles);
        superUserCheckRequests.getRequest(USERS).create(projectAdmin);

        // WHEN: ProjectAdmin creates a buildType for the project
        var buildType = testData.getBuildType();
        var requester = new UncheckedRequests(Specifications.authSpec(projectAdmin));
        var response = requester.getRequest(BUILD_TYPES).create(buildType);

        // THEN assert: OK
        response.then()
                .assertThat()
                .statusCode(SC_OK);
    }

    @Test(description = "Project Admin should NOT be able to create buildType for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        // GIVEN
        // SuperUser creates a project1
        var firstProject = testData.getProject();
        superUserCheckRequests.getRequest(PROJECTS).create(firstProject);

        // SuperUser creates a firstProjectAdmin assigned to the project1
        var userProjectAdminFirstProject = testData.getUser();
        var projectAdminRoleFirstProject = generate(Roles.class, "PROJECT_ADMIN", "p:" + testData.getProject().getId());
        userProjectAdminFirstProject.setRoles(projectAdminRoleFirstProject);
        superUserCheckRequests.getRequest(USERS).create(userProjectAdminFirstProject);

        // SuperUser creates a project2
        var secondTestData = generate();
        var secondProject = secondTestData.getProject();
        superUserCheckRequests.getRequest(PROJECTS).create(secondProject);

        // SuperUser creates a secondProjectAdmin assigned to the project2
        var userProjectAdminSecondProject = secondTestData.getUser();
        var projectAdminRoleSecondProject = generate(Roles.class, "PROJECT_ADMIN", "p:" + secondProject.getId());
        userProjectAdminSecondProject.setRoles(projectAdminRoleSecondProject);
        superUserCheckRequests.getRequest(USERS).create(userProjectAdminSecondProject);

        // WHEN: secondProjectAdmin creates a buildType for the project1
        var buildType = testData.getBuildType();
        buildType.setProject(firstProject);
        var requester = new UncheckedRequests(Specifications.authSpec(userProjectAdminSecondProject));
        var response = requester.getRequest(BUILD_TYPES).create(buildType);

        // THEN: BAD_REQUEST
        assertStatusCodeAndBody(response, SC_FORBIDDEN, "You do not have enough permissions to edit project with");
    }
}
