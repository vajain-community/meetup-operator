package com.k8s.meetup;

import com.k8s.meetup.apis.Condition;
import com.k8s.meetup.apis.ConditionStatus;
import com.k8s.meetup.apis.ConditionType;
import com.k8s.meetup.apis.MeetUpRequestStatus;

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
