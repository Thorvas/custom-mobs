package org.example.context;

import org.bukkit.Location;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.spell.Spell;

public class SpellContext {

    private Spell spell;
    private SpellTarget target;
    private SpellCaster caster;
    private Location castLocation;

    public Spell getSpell() {
        return spell;
    }

    public SpellTarget getTarget() {
        return target;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public void setTarget(SpellTarget target) {
        this.target = target;
    }

    public void setCaster(SpellCaster caster) {
        this.caster = caster;
    }

    public SpellCaster getCaster() {
        return caster;
    }
}
