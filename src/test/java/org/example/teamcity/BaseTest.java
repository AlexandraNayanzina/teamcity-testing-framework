package org.example.teamcity;

import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;

public class BaseTest {
    protected SoftAssert softy;
    private RequestSpecification superUserAuthorizationSpec = Specifications.superUserSpec();
    protected CheckedRequests superUserCheckRequests = new CheckedRequests(superUserAuthorizationSpec);
    protected UncheckedRequests superUserUncheckRequests = new UncheckedRequests(superUserAuthorizationSpec);
    protected TestData testData;

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        softy = new SoftAssert();
        testData = TestDataGenerator.generate();
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        softy.assertAll();
        TestDataStorage.getInstance().deleteCreatedEntity();
    }
}
