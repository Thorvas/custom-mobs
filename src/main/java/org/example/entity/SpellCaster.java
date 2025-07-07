package org.example.entity;

import org.bukkit.entity.LivingEntity;

import java.util.List;

public class SpellCaster {

    private LivingEntity caster;
    private List<PositiveEntityStatus> positiveStatuses;
    private List<NegativeEntityStatus> negativeStatuses;

    public LivingEntity getCaster() {
        return caster;
    }

    public void setCaster(LivingEntity caster) {
        this.caster = caster;
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
