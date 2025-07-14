package org.example.calculator.fireball;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.example.calculator.IMetaCalculator;
import org.example.context.SpellContext;
import org.example.context.SpellContextAttributes;
import org.example.spell.Spell;
import org.example.spell.UpgradeableSpell;
import org.example.spell.frostbolt.FireballSpell;

public class FireballMetaCalculator implements IMetaCalculator {

    public FireballMetaCalculator(FireballCalculateManager fireballCalculateManager) {
        this.fireballCalculateManager = fireballCalculateManager;
    }

    private final FireballCalculateManager fireballCalculateManager;

    @Override
    public Component getMeta(SpellContext context) {

        Double radius = context.getAttr(SpellContextAttributes.EXPLOSION_RADIUS);
        Double range = context.getAttr(SpellContextAttributes.MAX_RANGE);

        FireballSpell fireballSpell = (FireballSpell) context.getSpell();

        return Component.text("\n> Szczegółowe informacje o zaklęciu <", NamedTextColor.GRAY)
                .hoverEvent(HoverEvent.showText(
                        Component.text("[Szczegółowe informacje parametrów zaklęcia]\n", NamedTextColor.GOLD)
                                .append(Component.text("- Zasięg eksplozji: ", NamedTextColor.GRAY ).append(Component.text( radius + " bloków\n", NamedTextColor.RED))
                                .append(Component.text("- Obrażenia: ", NamedTextColor.GRAY).append(Component.text(String.format("%.2f", fireballCalculateManager.calculateDamage(context) / 2.0) + " ❤\n", NamedTextColor.RED))
                                        .append(Component.text("- Poziom zaklęcia: ", NamedTextColor.GRAY).append(Component.text("[" + fireballSpell.getLevel() + "]\n", NamedTextColor.GREEN))
                                                .append(Component.text("- Czas odnowienia księgi: ", NamedTextColor.GRAY).append(Component.text( fireballSpell.getCooldown() / 1000 + " sekund(y)\n", NamedTextColor.RED))
                                        .append(Component.text("- Zasięg rzucenia zaklęcia: ", NamedTextColor.GRAY).append(Component.text(range + " bloków\n", NamedTextColor.RED))
                                )))))));
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof FireballSpell;
    }
}
