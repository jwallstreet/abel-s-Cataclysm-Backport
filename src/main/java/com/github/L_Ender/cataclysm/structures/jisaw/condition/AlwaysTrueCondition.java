package com.github.L_Ender.cataclysm.structures.jisaw.condition;

import com.github.L_Ender.cataclysm.structures.jisaw.context.StructureContext;
import com.mojang.serialization.Codec;


/**
 * Condition that always passes.
 */
public class AlwaysTrueCondition  extends StructureCondition {
    private static final AlwaysTrueCondition INSTANCE = new AlwaysTrueCondition();
    public static final Codec<AlwaysTrueCondition> CODEC = Codec.unit(() -> INSTANCE);

    public AlwaysTrueCondition() {
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.ALWAYS_TRUE;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        return true;
    }
}
