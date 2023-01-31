package com.local.meetup;

import com.local.meetup.apis.Condition;
import com.local.meetup.apis.ConditionStatus;
import com.local.meetup.apis.ConditionType;
import com.local.meetup.apis.MeetUpRequestStatus;

import java.util.List;

public class StatusUtil {

    public static void updateStatusCondition(MeetUpRequestStatus meetUpRequestStatus, ConditionType type, ConditionStatus status) {
        Condition condition = getConditionByType(meetUpRequestStatus.getConditions(), type);
        condition.setStatus(status);
    }

    private static Condition getConditionByType(List<Condition> conditions, ConditionType type) {
        return conditions.stream().filter(c -> c.getType() == type).findFirst().get();
    }
}
