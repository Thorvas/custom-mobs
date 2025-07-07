package org.example.entity;

import org.bukkit.entity.LivingEntity;

import java.util.List;

public class SpellTarget {

    private LivingEntity target;
    private List<PositiveEntityStatus> positiveStatuses;
    private List<NegativeEntityStatus> negativeStatuses;

    public LivingEntity getTarget() {
        return target;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public List<PositiveEntityStatus> getPositiveStatuses() {
        return positiveStatuses;
    }

    public void setPositiveStatuses(List<PositiveEntityStatus> positiveStatuses) {
        this.positiveStatuses = positiveStatuses;
    }

    public List<NegativeEntityStatus> getNegativeStatuses() {
        return negativeStatuses;
    }

    public void setNegativeStatuses(List<NegativeEntityStatus> negativeStatuses) {
        this.negativeStatuses = negativeStatuses;
    }
}
