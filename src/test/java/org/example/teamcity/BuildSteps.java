package org.example.teamcity;

import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.models.Build;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.awaitility.Awaitility;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import static com.example.teamcity.api.enums.Endpoint.BUILDS;


public class BuildSteps extends BaseModel {


    public Build waitUntilBuildIsFinished(Build build, User user) {
        var atomicBuild = new AtomicReference<>(build);
        var checkedBuildRequest = new CheckedBase<Build>(Specifications.getSpec()
                .authSpec(user), BUILDS);
        Awaitility.await()
                .atMost(45, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> {
                    atomicBuild.set(checkedBuildRequest.read(atomicBuild.get().getId()));
                    return "finished".equals(atomicBuild.get().getState());
                });
        return atomicBuild.get();
    }


}
