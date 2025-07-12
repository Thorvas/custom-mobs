package org.example.calculator.meteor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.example.calculator.IMetaCalculator;
import org.example.context.SpellContext;
import org.example.spell.Spell;
import org.example.spell.meteor.MeteorSpell;

public class MeteorMetaCalculator implements IMetaCalculator {

    private final MeteorCalculateManager meteorCalculateManager;

    public MeteorMetaCalculator(MeteorCalculateManager meteorCalculateManager) {
        this.meteorCalculateManager = meteorCalculateManager;
    }

    @Override
    public Component getMeta(SpellContext context) {
        MeteorSpell meteorSpell = (MeteorSpell) context.getSpell();

        return Component.text("\n> Szczegółowe informacje o zaklęciu <", NamedTextColor.GRAY)
                .hoverEvent(HoverEvent.showText(
                        Component.text("[Szczegółowe informacje parametrów zaklęcia]\n", NamedTextColor.GOLD)
                                .append(Component.text("- Zasięg eksplozji: ", NamedTextColor.GRAY ).append(Component.text( meteorSpell.getEXPLOSION_RADIUS() + " bloków\n", NamedTextColor.RED))
                )));
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof MeteorSpell;
    }
}
