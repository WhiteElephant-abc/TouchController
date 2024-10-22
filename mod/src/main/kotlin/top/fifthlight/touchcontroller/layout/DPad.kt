package top.fifthlight.touchcontroller.layout

import net.minecraft.util.Colors
import top.fifthlight.touchcontroller.asset.Textures
import top.fifthlight.touchcontroller.control.DPad
import top.fifthlight.touchcontroller.control.DPadExtraButton

fun Context.DPad(config: DPad) {
    val buttonSize = config.buttonSize()
    val largeDisplaySize = config.largeDisplaySize()
    val smallDisplaySize = if (config.classic) {
        config.smallDisplaySize()
    } else {
        config.largeDisplaySize()
    }
    val offset = (largeDisplaySize - smallDisplaySize) / 2

    val forward = withRect(
        x = buttonSize.width,
        y = 0,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_forward") { clicked ->
            withAlign(
                align = Align.CENTER_CENTER,
                size = largeDisplaySize
            ) {
                when (Pair(config.classic, clicked)) {
                    Pair(true, false) -> Texture(id = Textures.DPAD_UP_CLASSIC)
                    Pair(true, true) -> Texture(id = Textures.DPAD_UP_CLASSIC, color = Colors.WHITE)
                    Pair(false, false) -> Texture(id = Textures.DPAD_UP)
                    Pair(false, true) -> Texture(id = Textures.DPAD_UP_ACTIVE)
                }
            }
        }.clicked
    }

    val backward = withRect(
        x = buttonSize.width,
        y = buttonSize.height * 2,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_backward") { clicked ->
            withAlign(
                align = Align.CENTER_CENTER,
                size = largeDisplaySize
            ) {
                when (Pair(config.classic, clicked)) {
                    Pair(true, false) -> Texture(id = Textures.DPAD_DOWN_CLASSIC)
                    Pair(true, true) -> Texture(id = Textures.DPAD_DOWN_CLASSIC, color = Colors.WHITE)
                    Pair(false, false) -> Texture(id = Textures.DPAD_DOWN)
                    Pair(false, true) -> Texture(id = Textures.DPAD_DOWN_ACTIVE)
                }
            }
        }.clicked
    }

    val left = withRect(
        x = 0,
        y = buttonSize.height,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_left") { clicked ->
            withAlign(
                align = Align.CENTER_CENTER,
                size = largeDisplaySize
            ) {
                when (Pair(config.classic, clicked)) {
                    Pair(true, false) -> Texture(id = Textures.DPAD_LEFT_CLASSIC)
                    Pair(true, true) -> Texture(id = Textures.DPAD_LEFT_CLASSIC, color = Colors.WHITE)
                    Pair(false, false) -> Texture(id = Textures.DPAD_LEFT)
                    Pair(false, true) -> Texture(id = Textures.DPAD_LEFT_ACTIVE)
                }
            }
        }.clicked
    }

    val right = withRect(
        x = buttonSize.width * 2,
        y = buttonSize.height,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        SwipeButton(id = "dpad_right") { clicked ->
            withAlign(
                align = Align.CENTER_CENTER,
                size = largeDisplaySize
            ) {
                when (Pair(config.classic, clicked)) {
                    Pair(true, false) -> Texture(id = Textures.DPAD_RIGHT_CLASSIC)
                    Pair(true, true) -> Texture(id = Textures.DPAD_RIGHT_CLASSIC, color = Colors.WHITE)
                    Pair(false, false) -> Texture(id = Textures.DPAD_RIGHT)
                    Pair(false, true) -> Texture(id = Textures.DPAD_RIGHT_ACTIVE)
                }
            }
        }.clicked
    }

    val showLeftForward = forward || left || status.dpadLeftForwardShown
    val showRightForward = forward || right || status.dpadRightForwardShown
    val showLeftBackward = !config.classic && (backward || left || status.dpadLeftBackwardShown)
    val showRightBackward = !config.classic && (backward || right || status.dpadRightBackwardShown)

    val leftForward = if (showLeftForward) {
        withRect(
            x = 0,
            y = 0,
            width = buttonSize.width,
            height = buttonSize.height
        ) {
            SwipeButton(id = "dpad_left_forward") { clicked ->
                withAlign(
                    align = Align.RIGHT_BOTTOM,
                    size = smallDisplaySize,
                    offset = offset,
                ) {
                    when (Pair(config.classic, clicked)) {
                        Pair(true, false) -> Texture(id = Textures.DPAD_UP_LEFT_CLASSIC)
                        Pair(true, true) -> Texture(id = Textures.DPAD_UP_LEFT_CLASSIC, color = Colors.WHITE)
                        Pair(false, false) -> Texture(id = Textures.DPAD_UP_LEFT)
                        Pair(false, true) -> Texture(id = Textures.DPAD_UP_LEFT_ACTIVE)
                    }
                }
            }.clicked
        }
    } else {
        false
    }

    val rightForward = if (showRightForward) {
        withRect(
            x = buttonSize.width * 2,
            y = 0,
            width = buttonSize.width,
            height = buttonSize.height
        ) {
            SwipeButton(id = "dpad_right_forward") { clicked ->
                withAlign(
                    align = Align.LEFT_BOTTOM,
                    size = smallDisplaySize,
                    offset = offset,
                ) {
                    when (Pair(config.classic, clicked)) {
                        Pair(true, false) -> Texture(id = Textures.DPAD_UP_RIGHT_CLASSIC)
                        Pair(true, true) -> Texture(id = Textures.DPAD_UP_RIGHT_CLASSIC, color = Colors.WHITE)
                        Pair(false, false) -> Texture(id = Textures.DPAD_UP_RIGHT)
                        Pair(false, true) -> Texture(id = Textures.DPAD_UP_RIGHT_ACTIVE)
                    }
                }
            }.clicked
        }
    } else {
        false
    }

    val leftBackward = if (showLeftBackward) {
        withRect(
            x = 0,
            y = buttonSize.height * 2,
            width = buttonSize.width,
            height = buttonSize.height
        ) {
            SwipeButton(id = "dpad_left_backward") { clicked ->
                withAlign(
                    align = Align.RIGHT_TOP,
                    size = smallDisplaySize,
                    offset = offset,
                ) {
                    if (clicked) {
                        Texture(id = Textures.DPAD_DOWN_LEFT_ACTIVE)
                    } else {
                        Texture(id = Textures.DPAD_DOWN_LEFT)
                    }
                }
            }.clicked
        }
    } else {
        false
    }

    val rightBackward = if (showRightBackward) {
        withRect(
            x = buttonSize.width * 2,
            y = buttonSize.width * 2,
            width = buttonSize.width,
            height = buttonSize.height
        ) {
            SwipeButton(id = "dpad_right_backward") { clicked ->
                withAlign(
                    align = Align.LEFT_TOP,
                    size = smallDisplaySize,
                    offset = offset,
                ) {
                    if (clicked) {
                        Texture(id = Textures.DPAD_DOWN_RIGHT_ACTIVE)
                    } else {
                        Texture(id = Textures.DPAD_DOWN_RIGHT)
                    }
                }
            }.clicked
        }
    } else {
        false
    }

    status.dpadLeftForwardShown = left || forward || leftForward
    status.dpadRightForwardShown = right || forward || rightForward
    status.dpadLeftBackwardShown = !config.classic && (left || backward || leftBackward)
    status.dpadRightBackwardShown = !config.classic && (right || backward || rightBackward)

    when (Pair(forward || leftForward || rightForward, backward || leftBackward || rightBackward)) {
        Pair(true, false) -> result.forward = 1f
        Pair(false, true) -> result.forward = -1f
    }

    when (Pair(left || leftForward || leftBackward, right || rightForward || rightBackward)) {
        Pair(true, false) -> result.left = 1f
        Pair(false, true) -> result.left = -1f
    }

    withRect(
        x = buttonSize.width,
        y = buttonSize.height,
        width = buttonSize.width,
        height = buttonSize.height
    ) {
        when (config.extraButton) {
            DPadExtraButton.NONE -> {}
            DPadExtraButton.SNEAK -> RawSneakButton(dpad = true, classic = config.classic, size = smallDisplaySize)
            DPadExtraButton.JUMP -> RawJumpButton(classic = config.classic, size = smallDisplaySize)
        }
    }
}