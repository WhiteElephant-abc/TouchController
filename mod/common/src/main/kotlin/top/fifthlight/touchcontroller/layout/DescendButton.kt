package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.assets.Textures
import top.fifthlight.touchcontroller.control.DescendButton
import top.fifthlight.touchcontroller.control.DescendButtonTexture

fun Context.DescendButton(config: DescendButton) {
    val (_, clicked) = SwipeButton(id = "descend") { clicked ->
        when (Pair(config.texture, clicked)) {
            Pair(DescendButtonTexture.CLASSIC, false) -> Texture(texture = Textures.GUI_DESCEND_DESCEND_CLASSIC)
            Pair(DescendButtonTexture.CLASSIC, true) -> Texture(
                texture = Textures.GUI_DESCEND_DESCEND_CLASSIC,
                color = 0xFFAAAAAAu
            )

            Pair(DescendButtonTexture.SWIMMING, false) -> Texture(texture = Textures.GUI_DESCEND_DESCEND_SWIMMING)
            Pair(DescendButtonTexture.SWIMMING, true) -> Texture(texture = Textures.GUI_DESCEND_DESCEND_SWIMMING_ACTIVE)
            Pair(DescendButtonTexture.FLYING, false) -> Texture(texture = Textures.GUI_DESCEND_DESCEND_FLYING)
            Pair(DescendButtonTexture.FLYING, true) -> Texture(texture = Textures.GUI_DESCEND_DESCEND_FLYING_ACTIVE)
        }
    }
    result.sneak = result.sneak || clicked
}