package com.ssblur.unfocused.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.gen.Invoker;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.gui.screens.Screen.class)
public interface ScreenAccessor {
    @Invoker
    static void callDefaultHandleGameClickEvent(ClickEvent arg, Minecraft arg2, @Nullable Screen arg3) {
        throw new UnsupportedOperationException();
    }
}
