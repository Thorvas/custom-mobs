package org.example.factory;

import net.kyori.adventure.text.Component;
import org.example.calculator.MetaExecutor;
import org.example.context.SpellContext;
import org.example.context.SpellContextAttributes;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.spell.Spell;

public class SpellDescriptionFactory {

    public SpellDescriptionFactory(MetaExecutor metaExecutor) {
        this.metaExecutor = metaExecutor;
    }

    private final MetaExecutor metaExecutor;


    public Component createSpellDescription(Spell spell) {

        SpellContext context = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);

        switch (spell.getId()) {
            case "FIREBALL" -> {
                var fb = (org.example.spell.frostbolt.FireballSpell) spell;
                context.addAttribute(SpellContextAttributes.EXPLOSION_RADIUS, fb.getExplosionRadius());
                context.addAttribute(SpellContextAttributes.MAX_RANGE, fb.getMaxRange());
            }
            case "METEOR" -> {
                var ms = (org.example.spell.meteor.MeteorSpell) spell;
                context.addAttribute(SpellContextAttributes.EXPLOSION_RADIUS, ms.getEXPLOSION_RADIUS());
                context.addAttribute(SpellContextAttributes.MAX_RANGE, ms.getMaxDistance());
            }
        }

        return spell.getTitle()
                .append(spell.getContent())
                .append(metaExecutor.calculateMeta(context));
    }
}
