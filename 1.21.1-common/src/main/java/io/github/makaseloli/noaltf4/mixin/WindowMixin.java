package io.github.makaseloli.noaltf4.mixin;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Window.class)
public class WindowMixin {
    private static final SystemToast.SystemToastId NOALTF4_CLOSE_BLOCKED = new SystemToast.SystemToastId();

    @Shadow
    @Final
    private long window;

    @Inject(method = "shouldClose", at = @At("HEAD"), cancellable = true)
    private void noaltf4$cancelWindowClose(CallbackInfoReturnable<Boolean> cir) {
        if (GLFW.glfwWindowShouldClose(this.window)) {
            GLFW.glfwSetWindowShouldClose(this.window, false);
            noaltf4$showBlockedToast();
            cir.setReturnValue(false);
        }
    }

    private static void noaltf4$showBlockedToast() {
        SystemToast.add(
            Minecraft.getInstance().getToasts(),
            NOALTF4_CLOSE_BLOCKED,
            Component.literal("No Alt F4"),
            Component.literal("Use Quit Game from the title screen.")
        );
    }
}
