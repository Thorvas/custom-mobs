package org.example.calculator.meteor;

import org.example.context.SpellContext;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.spell.meteor.MeteorSpell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MeteorDamageCalculatorTest {

    @Test
    void damageIncreasesWithExperience() {
        MeteorSpell spell = new MeteorSpell();
        MeteorDamageCalculator calc = new MeteorDamageCalculator();

        spell.setExperience(5);
        SpellContext ctxLow = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);
        double low = calc.calculateDamage(ctxLow);
        double expectedLow = (5d * 5) / (5 + 15);

        spell.setExperience(30);
        SpellContext ctxHigh = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);
        double high = calc.calculateDamage(ctxHigh);
        double expectedHigh = (5d * 30) / (30 + 15);

        assertTrue(high > low, "Damage should increase with experience");
        assertEquals(expectedLow, low);
        assertEquals(expectedHigh, high);
    }
}
