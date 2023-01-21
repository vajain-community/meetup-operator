package com.k8s.meetup.apis;

import java.util.Objects;

public class Condition {

    public Condition(ConditionType type) {
        this.type = type;
        this.status = ConditionStatus.UNKNOWN;
    }

    private ConditionType type;

    private ConditionStatus status;

    public ConditionType getType() {
        return type;
    }

    public void setType(ConditionType type) {
        this.type = type;
    }

    public ConditionStatus getStatus() {
        return status;
    }

    public void setStatus(ConditionStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return type == condition.type && status == condition.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, status);
    }
}
