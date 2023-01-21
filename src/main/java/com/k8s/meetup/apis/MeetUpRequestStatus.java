package com.k8s.meetup.apis;

import java.util.List;
import java.util.Objects;

import static com.k8s.meetup.apis.ConditionType.CONFIGMAP_READY;
import static com.k8s.meetup.apis.ConditionType.DEPLOYMENT_READY;

public class MeetUpRequestStatus {

    public MeetUpRequestStatus() {
        Condition configMapCondition = new Condition(CONFIGMAP_READY);
        Condition deploymentCondition = new Condition(DEPLOYMENT_READY);
        conditions = List.of(configMapCondition, deploymentCondition);
    }

    List<Condition> conditions;

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeetUpRequestStatus that = (MeetUpRequestStatus) o;
        return Objects.equals(conditions, that.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conditions);
    }
}
