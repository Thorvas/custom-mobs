package org.example.calculator.fireball;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.example.calculator.IMetaCalculator;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;

public class FireballMetaCalculator implements IMetaCalculator {

    public FireballMetaCalculator(FireballCalculateManager fireballCalculateManager) {
        this.fireballCalculateManager = fireballCalculateManager;
    }

    private final FireballCalculateManager fireballCalculateManager;

    @Override
    public Component getMeta(Spell spell) {

        FireballSpell fireballSpell = (FireballSpell) spell;

        return Component.text("\n> Szczegółowe informacje o zaklęciu <", NamedTextColor.GRAY)
                .hoverEvent(HoverEvent.showText(
                        Component.text("[Szczegółowe informacje parametrów zaklęcia]\n", NamedTextColor.GOLD)
                                .append(Component.text("- Zasięg eksplozji: ", NamedTextColor.GRAY ).append(Component.text( fireballSpell.getExplosionRadius() + " bloków\n", NamedTextColor.RED))
                                .append(Component.text("- Obrażenia: ", NamedTextColor.GRAY).append(Component.text(fireballCalculateManager.calculateDamage(fireballSpell) / 2.0 + " ❤\n", NamedTextColor.RED))
                                        .append(Component.text("- Zasięg rzucenia zaklęcia: ", NamedTextColor.GRAY).append(Component.text(fireballCalculateManager.calculateRange(fireballSpell) + " bloków\n", NamedTextColor.RED))
                                )))));
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof FireballSpell;
    }
}
