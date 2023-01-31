package com.local.meetup.apis;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("meetup.local.io")
@Version("v1alpha1")
@ShortNames("mr")
public class MeetUpRequest extends CustomResource<MeetUpRequestSpec, MeetUpRequestStatus> implements Namespaced {

    @Override
    protected MeetUpRequestSpec initSpec() {
        return new MeetUpRequestSpec();
    }

    @Override
    protected MeetUpRequestStatus initStatus() {
        return new MeetUpRequestStatus();
    }
}
