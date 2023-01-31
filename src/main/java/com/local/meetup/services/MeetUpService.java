package com.local.meetup.services;

import com.local.meetup.apis.MeetUpRequest;

public interface MeetUpService {
    boolean isMeetUpStatusChange(MeetUpRequest newMeetUpRequest);
}
