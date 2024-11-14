package top.fifthlight.touchcontroller.layout

import top.fifthlight.touchcontroller.asset.Textures
import top.fifthlight.touchcontroller.control.SneakButton
import top.fifthlight.touchcontroller.proxy.data.IntSize

fun Context.RawSneakButton(
    classic: Boolean = true,
    dpad: Boolean = false,
    size: IntSize = this.size
) {
    val (newPointer, _) = Button(id = "sneak") {
        withAlign(align = Align.CENTER_CENTER, size = size) {
            when (Triple(dpad, classic, status.sneakLocked)) {
                Triple(false, true, false), Triple(true, true, false) -> Texture(id = Textures.SNEAK_CLASSIC)
                Triple(false, true, true), Triple(true, true, true) -> Texture(id = Textures.SNEAK_CLASSIC_ACTIVE)
                Triple(true, false, false) -> Texture(id = Textures.SNEAK_DPAD)
                Triple(true, false, true) -> Texture(id = Textures.SNEAK_DPAD_ACTIVE)
                Triple(false, false, false) -> Texture(id = Textures.SNEAK)
                Triple(false, false, true) -> Texture(id = Textures.SNEAK_ACTIVE)
            }
        }
    }

    if (newPointer && status.sneakLocking.click(timer.tick)) {
        status.sneakLocked = !status.sneakLocked
    }
}

fun Context.SneakButton(config: SneakButton) {
    RawSneakButton(classic = config.classic, dpad = false)
}