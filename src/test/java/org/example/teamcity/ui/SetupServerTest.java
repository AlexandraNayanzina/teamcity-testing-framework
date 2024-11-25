package org.example.teamcity.ui;

import com.example.teamcity.ui.setup.FirstStartPage;
import org.testng.annotations.Test;

@Test(groups = {"Setup"})
public class SetupServerTest extends BaseUiTest{

    @Test(groups = {"Setup"})
    public void setupTeamCityServerTest() {
        FirstStartPage
                .open()
                .setupFirstStart();
    }
}
