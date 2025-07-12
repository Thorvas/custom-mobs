package org.example.entity;

import org.bukkit.entity.LivingEntity;
import org.example.entity.status.NegativeEntityStatus;
import org.example.entity.status.PositiveEntityStatus;
import org.example.spellbook.Spellbook;

import java.util.ArrayList;
import java.util.List;

public class SpellCaster {

    private LivingEntity caster;
    private Spellbook spellbook;
    private List<PositiveEntityStatus> positiveStatuses = new ArrayList<>();
    private List<NegativeEntityStatus> negativeStatuses = new ArrayList<>();

    public Spellbook getSpellbook() {
        return spellbook;
    }

    public void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }

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
