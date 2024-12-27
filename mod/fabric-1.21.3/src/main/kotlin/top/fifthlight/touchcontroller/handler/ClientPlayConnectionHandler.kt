package top.fifthlight.touchcontroller.handler

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.text.Text
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.combine.platform.toMinecraft
import top.fifthlight.touchcontroller.assets.Texts
import top.fifthlight.touchcontroller.platform.PlatformHolder
import top.fifthlight.touchcontroller.platform.proxy.ProxyPlatform

class ClientPlayConnectionHandler : ClientPlayConnectionEvents.Join, KoinComponent {
    private val platformHolder: PlatformHolder by inject()

    override fun onPlayReady(handler: ClientPlayNetworkHandler, sender: PacketSender, client: MinecraftClient) {
        val platform = platformHolder.platform
        if (platform == null) {
            client.inGameHud.chatHud.addMessage(Text.of(Texts.WARNING_PROXY_NOT_CONNECTED.toMinecraft()))
        } else if (platform is ProxyPlatform) {
            client.inGameHud.chatHud.addMessage(Text.of(Texts.WARNING_LEGACY_UDP_PROXY_USED.toMinecraft()))
        }
    }
}