package com.ssblur.unfocused.mixin;

import com.ssblur.unfocused.event.client.MouseScrollEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.joml.Vector2d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
  @Inject(
    method = "onScroll",
    at = @At("HEAD"),
    cancellable = true
  )
  public void unfocused$onScroll(
    long handle,
    double x,
    double y,
    CallbackInfo info
  ) {
    if (!info.isCancelled()) {
      MouseScrollEvent.INSTANCE.callback(
        new MouseScrollEvent.KeyPress(
          Minecraft.getInstance(),
          new Vector2d(x, y),
          MouseScrollEvent.INSTANCE
        )
      );
      if (MouseScrollEvent.INSTANCE.isCancelled()) info.cancel();
    }
  }
}
