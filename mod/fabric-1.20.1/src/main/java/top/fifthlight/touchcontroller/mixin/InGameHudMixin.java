package top.fifthlight.touchcontroller.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.koin.java.KoinJavaComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.fifthlight.touchcontroller.event.RenderEvents;
import top.fifthlight.touchcontroller.model.ControllerHudModel;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private static Identifier ICONS;
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void renderCrosshair(DrawContext context, CallbackInfo callbackInfo) {
        boolean shouldRender = RenderEvents.INSTANCE.shouldRenderCrosshair();
        if (!shouldRender) {
            if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.CROSSHAIR) {
                float attackCooldownProgress = this.client.player.getAttackCooldownProgress(0.0f);
                boolean renderFullTexture = false;
                if (this.client.targetedEntity != null && this.client.targetedEntity instanceof LivingEntity && attackCooldownProgress >= 1.0f) {
                    renderFullTexture = this.client.player.getAttackCooldownProgressPerTick() > 5.0f && this.client.targetedEntity.isAlive();
                }
                int x = context.getScaledWindowWidth() / 2;
                int y = context.getScaledWindowHeight() / 2;
                if (renderFullTexture) {
                    context.drawTexture(ICONS, x - 8, y - 8, 68, 94, 16, 16);
                } else if (attackCooldownProgress < 1.0f) {
                    int progress = (int) (attackCooldownProgress * 17.0f);
                    context.drawTexture(ICONS, x - 8, y - 2, 36, 94, 16, 4);
                    context.drawTexture(ICONS, x - 8, y - 2, 52, 94, progress, 4);
                }
            }
            RenderSystem.defaultBlendFunc();
            callbackInfo.cancel();
        }
    }

    @Inject(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V",
                    ordinal = 0
            )
    )
    private void renderHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
        var player = client.player;
        if (player != null) {
            var controllerHudModel = (ControllerHudModel) KoinJavaComponent.get(ControllerHudModel.class);
            var inventory = controllerHudModel.getResult().getInventory();
            var slots = inventory.getSlots();
            var x = (context.getScaledWindowWidth() - 182) / 2 + 1;
            var y = context.getScaledWindowHeight() - 22 + 1;
            for (int i = 0; i < 9; i++) {
                var stack = player.getInventory().getStack(i);
                if (stack.isEmpty()) {
                    continue;
                }
                var slot = slots[i];
                var progress = slot.getProgress();
                var height = (int) (16 * progress);
                context.fill(x + 20 * i + 2, y + 18 - height, x + 20 * i + 18, y + 18, 0xFF00BB00);
            }
        }
    }
}