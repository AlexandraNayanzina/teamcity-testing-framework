package com.example.teamcity.ui.pages.buildConfiguration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.admin.CreateBasePage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BuildTypeStatusPage extends CreateBasePage {
    // http://localhost:8111/buildConfiguration/SpringCoreForQa_Build1
    private static final String BUILD_TYPE_STATUS_URL = "/buildConfiguration/%s";


    private SelenideElement buildStatusRow = $$(".Details__button--h4").first();
    private SelenideElement successfulState = $(".MiddleEllipsis__searchable--uZ");

    public static BuildTypeStatusPage open(String buildTypeId) {
        return Selenide.open(BUILD_TYPE_STATUS_URL.formatted(buildTypeId), BuildTypeStatusPage.class);

    }

    public void verifyBuildTypeRunStatus () {
        buildStatusRow.should(Condition.visible, BASE_WAITING);
        successfulState.should(Condition.visible, BASE_WAITING);
    }

}
