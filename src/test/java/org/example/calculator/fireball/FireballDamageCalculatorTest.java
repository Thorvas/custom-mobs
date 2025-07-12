package org.example.calculator.fireball;

import org.example.context.SpellContext;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.spell.frostbolt.FireballSpell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FireballDamageCalculatorTest {

    @Test
    void damageIncreasesWithExperience() {
        FireballSpell spell = new FireballSpell();
        FireballDamageCalculator calc = new FireballDamageCalculator();

        // low experience
        spell.setExperience(5);
        SpellContext ctxLow = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);
        double low = calc.calculateDamage(ctxLow);
        double expectedLow = (5d * 5) / (5 + 15);

        // high experience
        spell.setExperience(20);
        SpellContext ctxHigh = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);
        double high = calc.calculateDamage(ctxHigh);
        double expectedHigh = (5d * 20) / (20 + 15);

        assertTrue(high > low, "Damage should increase with experience");
        assertEquals(expectedLow, low);
        assertEquals(expectedHigh, high);
    }
}
