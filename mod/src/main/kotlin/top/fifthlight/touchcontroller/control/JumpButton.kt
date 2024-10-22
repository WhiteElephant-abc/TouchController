package top.fifthlight.touchcontroller.control

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.minecraft.text.Text
import top.fifthlight.touchcontroller.asset.Texts
import top.fifthlight.touchcontroller.layout.Align
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.layout.JumpButton
import top.fifthlight.touchcontroller.proxy.data.IntOffset
import top.fifthlight.touchcontroller.proxy.data.IntSize
import kotlin.math.round

@Serializable
@SerialName("jump_button")
data class JumpButton(
    val size: Float = 2f,
    val classic: Boolean = true,
    override val align: Align = Align.RIGHT_BOTTOM,
    override val offset: IntOffset = IntOffset.ZERO,
    override val opacity: Float = 1f
) : ControllerWidget() {
    companion object {
        private val _properties = persistentListOf<Property<JumpButton, *, *>>(
            FloatProperty(
                getValue = { it.size },
                setValue = { config, value -> config.copy(size = value) },
                startValue = .5f,
                endValue = 4f,
                messageFormatter = {
                    Text.translatable(
                        Texts.OPTIONS_WIDGET_JUMP_BUTTON_PROPERTY_SIZE,
                        round(it * 100f).toString()
                    )
                },
            ),
            BooleanProperty(
                getValue = { it.classic },
                setValue = { config, value -> config.copy(classic = value) },
                message = Texts.OPTIONS_WIDGET_JUMP_BUTTON_PROPERTY_CLASSIC,
            )
        )
    }

    @Suppress("UNCHECKED_CAST")
    @Transient
    override val properties = super.properties + _properties as PersistentList<Property<ControllerWidget, *, *>>

    private val textureSize
        get() = if (classic) 18 else 22

    override fun size(): IntSize = IntSize((size * textureSize).toInt())

    override fun layout(context: Context) {
        context.JumpButton(this@JumpButton)
    }

    override fun cloneBase(align: Align, offset: IntOffset, opacity: Float) = copy(
        align = align,
        offset = offset,
        opacity = opacity
    )
}