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

        // low total XP
        spell.setExperience(10);
        SpellContext ctxLow = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);
        double low = calc.calculateDamage(ctxLow);
        int lowLevel = org.example.util.SpellExperienceUtil.levelForExperience(10);
        double expectedLow = (5d * lowLevel) / (lowLevel + 15);

        // high total XP
        spell.setExperience(50);
        SpellContext ctxHigh = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);
        double high = calc.calculateDamage(ctxHigh);
        int highLevel = org.example.util.SpellExperienceUtil.levelForExperience(50);
        double expectedHigh = (5d * highLevel) / (highLevel + 15);

        assertTrue(high > low, "Damage should increase with experience");
        assertEquals(expectedLow, low);
        assertEquals(expectedHigh, high);
    }
}
