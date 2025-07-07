package org.example.handler;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.example.context.SpellContext;

public class ApplyKnockbackHandlerCasting implements CastingPipelineHandler {

    @Override
    public void execute(SpellContext context) {
        LivingEntity target = context.getTarget().getTarget();
        if (target == null) return;

        // Źródło knockbacku — miejsce trafienia
        Location impactLoc = context.getHitLocation();
        if (impactLoc == null) {
            impactLoc = target.getLocation();
        }

        // Pobierz wektory pozycji
        Vector targetVec = target.getLocation().toVector();
        Vector sourceVec = impactLoc.toVector();

        // Wektor od CELU do ŹRÓDŁA (odwrotnie niż wcześniej)
        Vector direction = sourceVec.subtract(targetVec).normalize();

        double strength = context.getKnockbackStrength();

        // Teraz knockback będzie działał poprawnie
        if (strength > 0) target.knockback(strength, direction.getX(), direction.getZ());
    }
}
