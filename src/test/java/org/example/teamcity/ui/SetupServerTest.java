package org.example.teamcity.ui;

import com.example.teamcity.ui.setup.FirstStartPage;
import org.testng.annotations.Test;

public class SetupServerTest extends BaseUiTest{

    public SetupServerTest() {
    }


    @Test(groups = {"Setup"})
    public void setupTeamCityServerTest() {
        FirstStartPage
                .open()
                .setupFirstStart();
    }
}
