package org.example.handler;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.example.context.PreCastSpellContext;
import org.example.spell.Spell;
import org.example.spellbook.CooldownManager;

import java.util.UUID;

public class CheckCooldownStatusHandler implements PreCastPipelineHandler {

    private static final long COOLDOWN_MS = 3_000; // 3 sekundy

    private final CooldownManager cooldownManager;

    public CheckCooldownStatusHandler(CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    @Override
    public boolean execute(PreCastSpellContext context) {
        UUID pid = context.getCaster().getCaster().getUniqueId();

        if (cooldownManager.isOnCooldown(pid)) {
            long cooldownEnd = cooldownManager.getCooldowns().get(pid);
            long remMs       = cooldownEnd - System.currentTimeMillis();
            long rem        = (remMs + 999) / 1000;
            return true;
        }

        // pozwalamy rzucić i uruchamiamy cooldown
        Player player = (Player) context.getCaster().getCaster();
        cooldownManager.startCooldown(pid, context.getSpell().getCooldown());
        ItemStack offHand = player.getInventory().getItemInOffHand();
        player.setCooldown(offHand, (int) COOLDOWN_MS / 50); // Bukkit cooldown in ticks (20 ticks = 1 second)
        return false;
    }

    @Override
    public boolean supports(Spell spell) {
        return spell != null;
    }
}
