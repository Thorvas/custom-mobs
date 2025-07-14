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

        spell.setExperience(15);
        SpellContext ctxLow = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);
        double low = calc.calculateDamage(ctxLow);
        int lowLevel = org.example.util.SpellExperienceUtil.levelForExperience(15);
        double expectedLow = (5d * lowLevel) / (lowLevel + 15);

        spell.setExperience(60);
        SpellContext ctxHigh = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);
        double high = calc.calculateDamage(ctxHigh);
        int highLevel = org.example.util.SpellExperienceUtil.levelForExperience(60);
        double expectedHigh = (5d * highLevel) / (highLevel + 15);

        assertTrue(high > low, "Damage should increase with experience");
        assertEquals(expectedLow, low);
        assertEquals(expectedHigh, high);
    }
}
