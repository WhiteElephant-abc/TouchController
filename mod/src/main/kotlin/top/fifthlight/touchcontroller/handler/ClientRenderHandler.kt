package top.fifthlight.touchcontroller.handler

import net.minecraft.client.MinecraftClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.touchcontroller.config.TouchControllerConfigHolder
import top.fifthlight.touchcontroller.event.ClientRenderEvents
import top.fifthlight.touchcontroller.ext.scaledSize
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.layout.DrawQueue
import top.fifthlight.touchcontroller.layout.Hud
import top.fifthlight.touchcontroller.layout.HudState
import top.fifthlight.touchcontroller.mixin.ClientOpenChatScreenInvoker
import top.fifthlight.touchcontroller.model.ControllerHudModel
import top.fifthlight.touchcontroller.model.TouchStateModel
import top.fifthlight.touchcontroller.proxy.data.IntOffset

class ClientRenderHandler : ClientRenderEvents.StartRenderTick, KoinComponent {
    private val configHolder: TouchControllerConfigHolder by inject()
    private val controllerHudModel: ControllerHudModel by inject()
    private val touchStateModel: TouchStateModel by inject()

    override fun onStartTick(client: MinecraftClient, tick: Boolean) {
        val state = client.player?.let { player ->
            if (player.isSubmergedInWater) {
                HudState.SWIMMING
            } else if (player.abilities.flying) {
                HudState.FLYING
            } else {
                HudState.NORMAL
            }
        } ?: HudState.NORMAL
        if (state != HudState.NORMAL) controllerHudModel.status.sneakLocked = false
        val drawQueue = DrawQueue()
        val result = Context(
            drawQueue = drawQueue,
            size = client.window.scaledSize,
            screenOffset = IntOffset.ZERO,
            scale = client.window.scaleFactor.toFloat(),
            pointers = touchStateModel.pointers,
            status = controllerHudModel.status,
            timer = controllerHudModel.timer,
            state = state,
            config = configHolder.config.value
        ).run {
            Hud(
                widgets = configHolder.layout.value,
            )
            result
        }
        controllerHudModel.result = result
        if (state != HudState.NORMAL) controllerHudModel.status.sneakLocked = false
        controllerHudModel.pendingDrawQueue = drawQueue

        if (result.cancelFlying) {
            client.player?.abilities?.flying = false
        }
        if (result.chat) {
            (client as ClientOpenChatScreenInvoker).callOpenChatScreen("")
        }
        if (result.pause) {
            client.openGameMenu(false)
        }
        result.lookDirection?.let { (x, y) ->
            client.player?.changeLookDirection(x.toDouble(), y.toDouble())
        }
    }
}