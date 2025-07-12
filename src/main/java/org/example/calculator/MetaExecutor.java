package org.example.calculator;

import net.kyori.adventure.text.Component;
import org.example.context.SpellContext;
import org.example.spell.Spell;

import java.util.List;

public class MetaExecutor {

    private final List<IMetaCalculator> calculators;

    public MetaExecutor(List<IMetaCalculator> calculators) {
        this.calculators = calculators;
    }

    public Component calculateMeta(SpellContext context) {
        Spell spell = context.getSpell();
        return calculators.stream()
                .filter(calculator -> calculator.supports(spell))
                .findFirst()
                .map(calculator -> calculator.getMeta(context))
                .orElse(Component.empty());
    }
}
