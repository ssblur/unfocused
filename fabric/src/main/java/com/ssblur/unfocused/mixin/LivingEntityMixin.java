package com.ssblur.unfocused.mixin;

import com.ssblur.unfocused.event.common.EntityDamagedEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
  @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
  private void unfocused$hurt$pre(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
    LivingEntity self = (LivingEntity) (Object) this;
    EntityDamagedEvent.Companion.getBefore().callback(new EntityDamagedEvent.EntityDamage(self, damageSource, f, EntityDamagedEvent.Companion.getBefore()));
    if(EntityDamagedEvent.Companion.getBefore().isCancelled()) cir.setReturnValue(false);
  }


  @Inject(method = "hurt", at = @At("TAIL"))
  private void unfocused$hurt$post(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
    LivingEntity self = (LivingEntity) (Object) this;
    EntityDamagedEvent.Companion.getAfter().callback(new EntityDamagedEvent.EntityDamage(self, damageSource, f, EntityDamagedEvent.Companion.getAfter()));
  }
}
