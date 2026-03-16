package com.ssblur.unfocused.mixin;

import com.ssblur.unfocused.event.common.MobSpawnEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "addFreshEntity", at = @At("HEAD"), cancellable = true)
    private void unfocused$addFreshEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        ServerLevel self = (ServerLevel) (Object) this;
        if(entity instanceof Mob) {
            MobSpawnEvent.INSTANCE.callback(
                    new MobSpawnEvent.EntitySpawn(entity, self)
            );
            if(MobSpawnEvent.INSTANCE.isCancelled()) cir.setReturnValue(MobSpawnEvent.INSTANCE.getValue());
        }
    }
}
