package org.example.handler.meteor;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.example.context.SpellContext;
import org.example.context.SpellContextAttributes;
import org.example.handler.CastingPipelineHandler;
import org.example.spell.meteor.MeteorSpell;
import org.example.spell.Spell;

public class MeteorApplyKnockbackHandlerCasting implements CastingPipelineHandler {

    @Override
    public void execute(SpellContext context) {
        LivingEntity target = context.getTarget().getTarget();
        if (target == null) return;

        // Źródło knockbacku — miejsce trafienia
        Location impactLoc = context.getAttr(SpellContextAttributes.HIT_LOCATION);
        if (impactLoc == null) {
            impactLoc = target.getLocation();
        }

        // Pobierz wektory pozycji
        Vector targetVec = target.getLocation().toVector();
        Vector sourceVec = impactLoc.toVector();

        // Wektor od CELU do ŹRÓDŁA (odwrotnie niż wcześniej)
        Vector direction = sourceVec.subtract(targetVec).normalize();

        double strength = context.getAttr(SpellContextAttributes.KNOCKBACK);

        // Teraz knockback będzie działał poprawnie
        if (strength > 0) target.knockback(strength, direction.getX(), direction.getZ());
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof MeteorSpell;
    }
}
