package org.example.calculator;

import net.kyori.adventure.text.Component;
import org.example.spell.Spell;

import java.util.List;

public class MetaExecutor {

    private final List<IMetaCalculator> calculators;

    public MetaExecutor(List<IMetaCalculator> calculators) {
        this.calculators = calculators;
    }

    public Component calculateMeta(Spell spell) {
        return calculators.stream()
                .filter(calculator -> calculator.supports(spell))
                .findFirst()
                .map(calculator -> calculator.getMeta(spell))
                .orElse(Component.empty());
    }
}
