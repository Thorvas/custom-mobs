package org.example.calculator.meteor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.example.calculator.ICalculateManager;
import org.example.context.SpellContext;
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

    public double calculateDamage(SpellContext context) {
        return meteorDamageCalculator.calculateDamage(context);
    }

    public double calculateKnockback(SpellContext context, double distance) {
        return meteorKnockbackCalculator.calculateKnockback(context, distance);
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof MeteorSpell;
    }
}
