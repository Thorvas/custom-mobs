package org.example.handler;

import org.example.context.SpellContext;

public interface ProjectileStrategy {

    CastInput build(SpellContext context);
}
