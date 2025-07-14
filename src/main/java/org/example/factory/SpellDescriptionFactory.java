package org.example.factory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.example.calculator.MetaContextResolver;
import org.example.calculator.MetaExecutor;
import org.example.context.SpellContext;
import org.example.entity.SpellCaster;
import org.example.entity.SpellTarget;
import org.example.spell.Spell;
import org.example.spell.UpgradeableSpell;
import org.example.spellbook.SpellManager;
import org.example.spellbook.Spellbook;
import org.example.util.ExperienceUtil;

public class SpellDescriptionFactory {

    public SpellDescriptionFactory(MetaExecutor metaExecutor, MetaContextResolver metaContextResolver, SpellManager spellManager) {
        this.metaExecutor = metaExecutor;
        this.metaContextResolver = metaContextResolver;
        this.spellManager = spellManager;
    }

    private final MetaExecutor metaExecutor;
    private final MetaContextResolver metaContextResolver;
    private final SpellManager spellManager;


    public Component createSpellDescription(Spell spell, Player player) {

        SpellContext context = new SpellContext(spell, new SpellTarget(), new SpellCaster(), 0);

        metaContextResolver.resolveContext(context, spell);

        return spell.getTitle()
                .append(spell.getContent())
                .append(metaExecutor.calculateMeta(context))
                .append(Component.text("\n\n> Zwiększ poziom zaklęcia <", NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.showText(
                                Component.text("Użyj obecnie posiadanego doświadczenia do wzmocnienia zaklęcia", NamedTextColor.GOLD)))
                        .clickEvent(ClickEvent.callback(spellCallback(player, spell, ExperienceUtil.getTotalExperience(player)))));
    }

    public ClickCallback spellCallback(Player p, Spell spell, int expAmount) {
        return context -> {
            if (expAmount <= 0) {
                p.closeInventory();
                p.sendMessage("Ilość XP musi być większa od zera.");
                return;
            }
            int totalXp = ExperienceUtil.getTotalExperience(p);
            if (totalXp < expAmount) {
                p.closeInventory();
                p.sendMessage("Nie masz wystarczającego doświadczenia.");
                return;
            }
            Spellbook book = spellManager.loadSpellbookFor(p);
            Spell target = book.getKnownSpells().stream()
                    .filter(s -> s.getId().equalsIgnoreCase(spell.getId()))
                    .findFirst()
                    .orElse(null);
            if (!(target instanceof UpgradeableSpell up)) {
                p.sendMessage("Nie można ustawić doświadczenia tego zaklęcia.");
                p.closeInventory();
                return;
            }

            int previousLevel = up.getLevel();

            up.addExperience(expAmount);

            checkLevelUp(up, previousLevel, p);

            ExperienceUtil.setTotalExperience(p, totalXp - expAmount);
            spellManager.saveSpellbookFor(p);

            p.closeInventory();
            p.sendMessage("Dodano " + expAmount + " XP do zaklęcia "
                    + up.getName() + ". Poziom: " + up.getLevel()
                    + " (do następnego potrzeba " + up.experienceToNextLevel() + " XP).");
        };
    }

    private void checkLevelUp(UpgradeableSpell spell, int previousLevel, Player player) {
            if (spell.getLevel() > previousLevel) {
                int levelsGained = spell.getLevel() - previousLevel;
                player.sendMessage("Gratulacje! Zdobyłeś " + levelsGained + " poziom(y) w zaklęciu " + spell.getName() + "!");
                playLevelUpEffects(player, levelsGained);
            }
        }



    /**
     * Plays particle effects and sound when a player levels up.
     */
    private static void playLevelUpEffects(Player player, int levelsGained) {
        Location playerLoc = player.getLocation().add(0, 1, 0); // Slightly above player

        // Play enchantment sound (level up sound)
        player.playSound(playerLoc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        // Spawn enchantment particles around the player
        player.getWorld().spawnParticle(Particle.ENCHANT,
                playerLoc,
                30, // particle count
                0.5, 0.5, 0.5, // spread in x, y, z
                0.1 // particle speed
        );

        // Add firework particles for dramatic effect
        player.getWorld().spawnParticle(Particle.FIREWORK,
                playerLoc,
                20,
                0.3, 0.3, 0.3,
                0.05
        );

        // For multiple levels gained, add extra effects
        if (levelsGained > 1) {
            // Play additional celebratory sound
            player.playSound(playerLoc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.5f, 1.2f);

            // Add more dramatic particles
            player.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING,
                    playerLoc,
                    15,
                    0.4, 0.4, 0.4,
                    0.1
            );
        }
    }

}
