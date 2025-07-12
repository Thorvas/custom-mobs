package org.example.handler.fireball;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.example.context.VisualSpellContext;
import org.example.context.VisualSpellContextAttributes;
import org.example.handler.VisualPipelineHandler;
import org.example.spell.Spell;
import org.example.spell.frostbolt.FireballSpell;

public class FireballVisualInitialCastHandler implements VisualPipelineHandler {
    @Override
    public void execute(VisualSpellContext context) {
        Player player = (Player) context.getCaster().getCaster();
        FireballSpell meteorSpell = (FireballSpell) context.getSpell();
        World world = player.getWorld();
        Location origin = player.getEyeLocation();
        Vector direction = origin.getDirection().normalize();

        // Cząsteczki otoczki i efektu końcowego
        context.addAttribute(VisualSpellContextAttributes.WORLD, world);
        context.addAttribute(VisualSpellContextAttributes.ORIGIN, origin);
        context.addAttribute(VisualSpellContextAttributes.DIRECTION, direction);
        context.addAttribute(VisualSpellContextAttributes.SPEED, meteorSpell.getSpeed());
        context.addAttribute(VisualSpellContextAttributes.MAX_RANGE, meteorSpell.getMaxRange());
        context.addAttribute(VisualSpellContextAttributes.EXPLOSION_RADIUS, meteorSpell.getExplosionRadius());
        context.addAttribute(VisualSpellContextAttributes.EXPLOSION_POWER, meteorSpell.getExplosionPower());
        context.addAttribute(VisualSpellContextAttributes.BASE_DAMAGE, meteorSpell.getBaseDamage());
        context.addAttribute(VisualSpellContextAttributes.PARTICLE_TRAIL, Particle.FLAME);
        context.addAttribute(VisualSpellContextAttributes.PARTICLE_HIT, Particle.EXPLOSION);

        // Przekaż też niezbędne obiekty z kontekstu:
        context.addAttribute(VisualSpellContextAttributes.DAMAGE_CALCULATOR,
                context.getAttr(VisualSpellContextAttributes.DAMAGE_CALCULATOR));
        context.addAttribute(VisualSpellContextAttributes.EXECUTOR,
                context.getAttr(VisualSpellContextAttributes.EXECUTOR));
        context.addAttribute(VisualSpellContextAttributes.PLUGIN,
                context.getAttr(VisualSpellContextAttributes.PLUGIN));

        context.addAttribute(VisualSpellContextAttributes.SPELL, context.getSpell());
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof FireballSpell;
    }
}
