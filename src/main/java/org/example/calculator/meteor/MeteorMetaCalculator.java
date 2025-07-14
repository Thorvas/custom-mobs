package org.example.calculator.meteor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.example.calculator.IMetaCalculator;
import org.example.context.SpellContext;
import org.example.context.SpellContextAttributes;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;
import org.example.spell.meteor.MeteorSpell;
import org.example.util.ExperienceUtil;
import org.example.util.SpellExperienceUtil;

public class MeteorMetaCalculator implements IMetaCalculator {

    private final MeteorCalculateManager meteorCalculateManager;

    public MeteorMetaCalculator(MeteorCalculateManager meteorCalculateManager) {
        this.meteorCalculateManager = meteorCalculateManager;
    }

    @Override
    public Component getMeta(SpellContext context) {
        Double radius = context.getAttr(SpellContextAttributes.EXPLOSION_RADIUS);

        MeteorSpell meteorSpell = (MeteorSpell) context.getSpell();

        int level = meteorSpell.getLevel(); // derived from total XP
        int xpForCurrentLevel = SpellExperienceUtil.experienceForLevel(level);
        int progress = meteorSpell.getExperience() - xpForCurrentLevel;
        int needed = SpellExperienceUtil.experienceToNextLevel(level);
        double percent = (double) progress * 100 / needed;
        String percentText = String.format("%.1f", percent); // jedna cyfra po przecinku

        return Component.text("\n> Szczegółowe informacje o zaklęciu <", NamedTextColor.GRAY)
                .hoverEvent(HoverEvent.showText(
                        Component.text("[Szczegółowe informacje parametrów zaklęcia]\n", NamedTextColor.GOLD)
                                .append(Component.text("- Obrażenia: ", NamedTextColor.GRAY).append(Component.text(String.format("%.2f",meteorCalculateManager.calculateDamage(context) / 2.0) + " ❤\n", NamedTextColor.RED))
                                        .append(Component.text("- Czas odnowienia księgi: ", NamedTextColor.GRAY).append(Component.text( meteorSpell.getCooldown() / 1000 + " sekund(y) \n", NamedTextColor.RED))
                                        .append(Component.text("- Poziom zaklęcia: ", NamedTextColor.GRAY).append(Component.text("[" + meteorSpell.getLevel() + "]\n", NamedTextColor.GREEN))
                                                .append(Component.text("- Doświadczenie zaklęcia: ", NamedTextColor.GRAY).append(Component.text("[" + progress + "/" + needed + "] " + "("+percentText+"%)" + "\n", NamedTextColor.GREEN))
                                .append(Component.text("- Zasięg eksplozji: ", NamedTextColor.GRAY ).append(Component.text( radius + " bloków\n", NamedTextColor.RED))
                )))))));
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof MeteorSpell;
    }
}
