package com.local.meetup.services;

import com.local.meetup.apis.MeetUpRequest;
import io.fabric8.kubernetes.client.KubernetesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MeetUpServiceImpl implements MeetUpService {

    @Inject
    KubernetesClient kubernetesClient;

    @Override
    public boolean isMeetUpStatusChange(MeetUpRequest newMeetUpRequest) {
        MeetUpRequest oldMeetUpRequest = fetchMeetUpRequest(newMeetUpRequest.getMetadata().getName(), newMeetUpRequest.getMetadata().getNamespace());
        if (oldMeetUpRequest == null) {
            return true;
        }
        return oldMeetUpRequest.getStatus().equals(newMeetUpRequest.getStatus());
    }

    private MeetUpRequest fetchMeetUpRequest(String name, String namespace) {
        return kubernetesClient.resources(MeetUpRequest.class)
                .inNamespace(namespace)
                .withName(name)
                .get();
    }
}
