package org.example.calculator;

import org.example.context.SpellContext;
import org.example.context.SpellContextAttributes;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;
import org.example.spell.meteor.MeteorSpell;

import java.util.Optional;

public class MetaContextResolver {

    public void resolveContext(SpellContext context, Spell spell) {
        Optional.ofNullable(spell)
            .filter(FireballSpell.class::isInstance)
            .map(FireballSpell.class::cast)
            .ifPresent(fb -> {
                context.addAttribute(SpellContextAttributes.EXPLOSION_RADIUS, fb.getExplosionRadius());
                context.addAttribute(SpellContextAttributes.MAX_RANGE, fb.getMaxRange());
            });

        Optional.ofNullable(spell)
            .filter(MeteorSpell.class::isInstance)
            .map(MeteorSpell.class::cast)
            .ifPresent(ms -> {
                context.addAttribute(SpellContextAttributes.EXPLOSION_RADIUS, ms.getEXPLOSION_RADIUS());
                context.addAttribute(SpellContextAttributes.MAX_RANGE, ms.getMaxDistance());
            });
    }
}
