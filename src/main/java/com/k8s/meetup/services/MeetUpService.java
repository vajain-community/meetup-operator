package com.k8s.meetup.services;

import com.k8s.meetup.apis.MeetUpRequest;

public interface MeetUpService {
    boolean isMeetUpStatusChange(MeetUpRequest newMeetUpRequest);
}
