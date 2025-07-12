package org.example.factory;

import net.kyori.adventure.text.Component;
import org.example.calculator.MetaExecutor;
import org.example.spell.Spell;

public class SpellDescriptionFactory {

    public SpellDescriptionFactory(MetaExecutor metaExecutor) {
        this.metaExecutor = metaExecutor;
    }

    private final MetaExecutor metaExecutor;


    public Component createSpellDescription(Spell spell) {

        return spell.getTitle()
                .append(spell.getContent())
                .append(metaExecutor.calculateMeta(spell));
    }
}
