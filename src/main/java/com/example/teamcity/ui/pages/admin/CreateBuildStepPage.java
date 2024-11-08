package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.BasePage;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;


public class CreateBuildStepPage extends BasePage {
    protected static final String BUILD_STEP_URL = "/admin/editRunType.html?id=buildType:%s&runnerId=__NEW_RUNNER__";

    // http://localhost:8111/admin/editRunType.html?id=buildType:test_CZhvxqGKuY_Build321&runnerId=__NEW_RUNNER__

    // http://localhost:8111/admin/editRunType.html?id=buildType:test_dngjZMitxB&runnerId=__NEW_RUNNER__

    protected SelenideElement commandLineStep = $("tr[data-test='runner-item simpleRunner']");
    protected SelenideElement buildStepName = $("#buildStepName");
    protected SelenideElement stepId = $("#newRunnerId");
    protected SelenideElement saveButton = $("input[value='Save'][name='submitButton']");
    protected SelenideElement runButton = $("#breadcrumbsWrapper > div.quickLinks > div:nth-child(1) > span > button:nth-child(1)");

    public static CreateBuildStepPage open(String buildTypeId) {
        return Selenide.open(BUILD_STEP_URL.formatted(buildTypeId), CreateBuildStepPage.class);
    }


    public void createBuildStep(String stepName) {
        commandLineStep.should(Condition.visible, BASE_WAITING);
        commandLineStep.click();
        buildStepName.should(Condition.visible, BASE_WAITING);
        buildStepName.val(stepName);
        executeJavaScript("document.querySelector('.CodeMirror').CodeMirror.setValue('echo \"Hello, world\"');");
        saveButton.click();
    }
}
