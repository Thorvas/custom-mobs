package org.example.handler;

import org.example.context.PreCastSpellContext;
import org.example.entity.status.NegativeEntityStatus;
import org.example.spell.InterruptibleSpell;
import org.example.spell.Spell;

public class CheckStunnedStatusHandler implements PreCastPipelineHandler {

    @Override
    public boolean execute(PreCastSpellContext context) {
        return context.getCaster().getNegativeStatuses().stream()
                .anyMatch(status -> status.equals(NegativeEntityStatus.STUNNED));
    }

    @Override
    public boolean supports(Spell spell) {
        return spell instanceof InterruptibleSpell;
    }
}
