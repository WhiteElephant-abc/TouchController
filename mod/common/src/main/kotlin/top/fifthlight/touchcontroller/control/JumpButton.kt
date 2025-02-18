package top.fifthlight.touchcontroller.control

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import top.fifthlight.combine.data.TextFactory
import top.fifthlight.data.IntOffset
import top.fifthlight.data.IntSize
import top.fifthlight.touchcontroller.assets.Texts
import top.fifthlight.touchcontroller.layout.Align
import top.fifthlight.touchcontroller.layout.Context
import top.fifthlight.touchcontroller.layout.JumpButton
import kotlin.math.round

@Serializable
enum class JumpButtonTexture {
    @SerialName("classic")
    CLASSIC,

    @SerialName("classic_flying")
    CLASSIC_FLYING,

    @SerialName("new")
    NEW,

    @SerialName("new_horse")
    NEW_HORSE,
}

@Serializable
@SerialName("jump_button")
data class JumpButton(
    val size: Float = 2f,
    val texture: JumpButtonTexture = JumpButtonTexture.CLASSIC,
    override val align: Align = Align.RIGHT_BOTTOM,
    override val offset: IntOffset = IntOffset.ZERO,
    override val opacity: Float = 1f
) : ControllerWidget() {
    companion object : KoinComponent {
        private val textFactory: TextFactory by inject()

        @Suppress("UNCHECKED_CAST")
        private val _properties = baseProperties + persistentListOf<Property<JumpButton, *>>(
            FloatProperty(
                getValue = { it.size },
                setValue = { config, value -> config.copy(size = value) },
                range = .5f..4f,
                messageFormatter = {
                    textFactory.format(
                        Texts.SCREEN_OPTIONS_WIDGET_JUMP_BUTTON_PROPERTY_SIZE,
                        round(it * 100f).toString()
                    )
                },
            ),
            EnumProperty(
                getValue = { it.texture },
                setValue = { config, value -> config.copy(texture = value) },
                name = textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_JUMP_BUTTON_PROPERTY_STYLE),
                items = persistentListOf(
                    JumpButtonTexture.CLASSIC to textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_JUMP_BUTTON_PROPERTY_STYLE_CLASSIC),
                    JumpButtonTexture.CLASSIC_FLYING to textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_JUMP_BUTTON_PROPERTY_STYLE_CLASSIC_FLYING),
                    JumpButtonTexture.NEW to textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_JUMP_BUTTON_PROPERTY_STYLE_NEW),
                    JumpButtonTexture.NEW_HORSE to textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_JUMP_BUTTON_PROPERTY_STYLE_NEW_HORSE),
                )
            )
        ) as PersistentList<Property<ControllerWidget, *>>
    }

    override val properties
        get() = _properties

    private val textureSize
        get() = if (texture == JumpButtonTexture.NEW || texture == JumpButtonTexture.NEW_HORSE) 22 else 18

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