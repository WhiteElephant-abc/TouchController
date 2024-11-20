package top.fifthlight.touchcontroller.state

import top.fifthlight.touchcontroller.proxy.data.Offset

sealed class PointerState {
    data object New : PointerState()
    data class View(
        val initialPosition: Offset,
        val lastPosition: Offset,
        val moving: Boolean = false,
        val longPressTriggered: Boolean = false,
        val consumed: Boolean = false,
        val pressTime: Int,
    ) : PointerState()

    data object Joystick : PointerState()
    data object Invalid : PointerState()
    data class Released(
        val previousPosition: Offset,
        val previousState: PointerState
    ) : PointerState() {
        init {
            require(previousState !is Released)
        }
    }

    data class Button(val id: String) : PointerState()
    data class SwipeButton(val id: String) : PointerState()
}

data class Pointer(
    var position: Offset,
    var state: PointerState = PointerState.New
)
