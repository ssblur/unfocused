package com.ssblur.unfocused.mixin;

import com.mojang.datafixers.util.Pair;
import com.ssblur.unfocused.biome.TemplatePoolInjects;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(StructureTemplatePool.class)
public class StructureTemplatePoolMixin {
    @Final
    @Shadow
    private List<Pair<StructurePoolElement, Integer>> rawTemplates;
    @Final
    @Shadow
    private ObjectArrayList<StructurePoolElement> templates;

    @Inject(method = "<init>(Lnet/minecraft/core/Holder;Ljava/util/List;)V", at = @At("TAIL"))
    private void unfocused$injectInit(
            Holder<StructureTemplatePool> holder,
            List<Pair<StructurePoolElement, Integer>> list,
            CallbackInfo ci
    ) {
        TemplatePoolInjects.INSTANCE.inject(holder, rawTemplates, templates, (StructureTemplatePool) (Object) this);
    }

    @Inject(method = "<init>(Lnet/minecraft/core/Holder;Ljava/util/List;Lnet/minecraft/world/level/levelgen/structure/pools/StructureTemplatePool$Projection;)V", at = @At("TAIL"))
    private void unfocused$injectInit(
            Holder<StructureTemplatePool> holder,
            List<Pair<StructurePoolElement, Integer>> list,
            StructureTemplatePool.Projection projection,
            CallbackInfo ci
    ) {
        TemplatePoolInjects.INSTANCE.inject(holder, rawTemplates, templates, (StructureTemplatePool) (Object) this);
    }
}
