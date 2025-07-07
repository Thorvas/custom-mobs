package org.example.context;

import org.bukkit.Location;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.spell.Spell;

public class SpellContext {

    private final Spell spell;
    private final SpellTarget target;
    private final Location hitLocation;
    private final double knockbackStrength;
    private final double damage;
    private final SpellCaster caster;

    public Location getHitLocation() {
        return hitLocation;
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

    public double getKnockbackStrength() {
        return knockbackStrength;
    }

    public SpellContext(Spell spell, SpellTarget target, SpellCaster caster, Location hitLocation, double damage, double knockbackStrength) {
        this.spell = spell;
        this.target = target;
        this.caster = caster;
        this.hitLocation = hitLocation;
        this.damage = damage;
        this.knockbackStrength = knockbackStrength;
    }
}
