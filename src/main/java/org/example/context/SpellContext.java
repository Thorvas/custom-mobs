package org.example.context;

import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.spell.Spell;

import java.util.HashMap;
import java.util.Map;

public class SpellContext {

    private final Spell spell;
    private final SpellTarget target;
    private Map<SpellContextAttributes, Object> metadata = new HashMap<>();
    private final double damage;
    private final SpellCaster caster;

    public <T> void addAttribute(SpellContextAttributes key, T value) {
        metadata.put(key, value);
    }

    public <T> T getAttr(SpellContextAttributes key) {
        return metadata.get(key) != null ? (T) metadata.get(key) : null;
    }

    public double getDamage() {
        return damage;
    }

    public Spell getSpell() {
        return spell;
    }

    public SpellTarget getTarget() {
        return target;
    }

    public SpellCaster getCaster() {
        return caster;
    }

    public SpellContext(Spell spell, SpellTarget target, SpellCaster caster, double damage) {
        this.spell = spell;
        this.target = target;
        this.caster = caster;
        this.damage = damage;
    }
}
