package org.example.calculator.meteor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.example.calculator.ICalculateManager;
import org.example.spell.Spell;
import org.example.spell.meteor.MeteorSpell;

public class MeteorCalculateManager implements ICalculateManager {

    private final MeteorDamageCalculator meteorDamageCalculator;
    private final MeteorKnockbackCalculator meteorKnockbackCalculator;

    public MeteorCalculateManager(MeteorDamageCalculator meteorDamageCalculator,
                                  MeteorKnockbackCalculator meteorKnockbackCalculator) {
        this.meteorDamageCalculator = meteorDamageCalculator;
        this.meteorKnockbackCalculator = meteorKnockbackCalculator;
    }

    public double calculateDamage(Spell spell) {
        return meteorDamageCalculator.calculateDamage(spell);
    }

    public double calculateKnockback(Spell spell, double distance) {
        return meteorKnockbackCalculator.calculateKnockback(spell, distance);
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof MeteorSpell;
    }
}
