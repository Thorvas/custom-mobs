package org.example.handler;

import org.example.context.PreCastSpellContext;
import org.example.entity.NegativeEntityStatus;

public class CheckStunnedStatusHandler implements PreCastPipelineHandler {

    @Override
    public boolean execute(PreCastSpellContext context) {
        return context.caster().getNegativeStatuses().stream()
                .anyMatch(status -> status.getName().equals(NegativeEntityStatus.STUNNED.getName()));
    }
}
