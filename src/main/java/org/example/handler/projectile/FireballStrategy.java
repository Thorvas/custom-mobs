package org.example.handler.projectile;

import org.example.context.SpellContext;
import org.example.handler.CastInput;
import org.example.handler.ProjectileStrategy;

public class FireballStrategy implements ProjectileStrategy {

    @Override
    public CastInput build(SpellContext context) {
        CastInput castInput = new CastInput(context.getCaster().getCaster().getEyeLocation(), context.getCaster().getCaster().getEyeLocation().getDirection(), null);

        return castInput;
    }
}
