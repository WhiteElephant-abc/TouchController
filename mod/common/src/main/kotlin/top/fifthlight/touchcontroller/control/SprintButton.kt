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
import top.fifthlight.touchcontroller.layout.SprintButton
import kotlin.math.round

@Serializable
enum class SprintButtonTexture {
    @SerialName("new")
    NEW,

    @SerialName("classic")
    CLASSIC,
}

@Serializable
enum class SprintButtonTrigger{
    @SerialName("single_click_lock")
    SINGLE_CLICK_LOCK,

    @SerialName("hold")
    HOLD,

}

@Serializable
@SerialName("sprint_button")
data class SprintButton(
    val size: Float = 2f,
    val texture: SprintButtonTexture = SprintButtonTexture.CLASSIC,
    val trigger: SprintButtonTrigger = SprintButtonTrigger.SINGLE_CLICK_LOCK,
    override val align: Align = Align.RIGHT_BOTTOM,
    override val offset: IntOffset = IntOffset(32, 68),
    override val opacity: Float = 1f,
): ControllerWidget(){
    companion object : KoinComponent {
        private val textFactory: TextFactory by inject()

        @Suppress("UNCHECKED_CAST")
        private val _properties = baseProperties + persistentListOf<Property<SprintButton, *>>(
            FloatProperty(
                getValue = { it.size },
                setValue = { config, value -> config.copy(size = value) },
                range = .5f..4f,
                messageFormatter = {
                    textFactory.format(
                        Texts.SCREEN_OPTIONS_WIDGET_SPRINT_BUTTON_PROPERTY_SIZE,
                        round(it * 100f).toString()
                    )
                }
            ),
            EnumProperty(
                getValue = { it.texture },
                setValue = { config, value -> config.copy(texture = value) },
                name = textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_SPRINT_BUTTON_PROPERTY_STYLE),
                items = persistentListOf(
                    SprintButtonTexture.NEW to textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_SPRINT_BUTTON_PROPERTY_STYLE_NEW),
                    SprintButtonTexture.CLASSIC to textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_SPRINT_BUTTON_PROPERTY_STYLE_CLASSIC)
                )
            ),
            EnumProperty(
                getValue = { it.trigger },
                setValue = { config, value -> config.copy(trigger = value) },
                name = textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_JOYSTICK_PROPERTY_TRIGGER_SPRINT),
                items = persistentListOf(
                    SprintButtonTrigger.HOLD to textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_SPRINT_BUTTON_PROPERTY_TRIGGER_HOLD),
                    SprintButtonTrigger.SINGLE_CLICK_LOCK to textFactory.of(Texts.SCREEN_OPTIONS_WIDGET_SPRINT_BUTTON_PROPERTY_TRIGGER_SINGLE_CLICK_LOCK),
                )
            )
        ) as PersistentList<Property<ControllerWidget, *>>
    }

    override val properties
        get() = _properties

    private val textureSize
        get() = if(texture == SprintButtonTexture.CLASSIC) 18 else 22

    override fun size(): IntSize = IntSize((size * textureSize).toInt())

    override fun layout(context: Context) {
        context.SprintButton(this@SprintButton)
    }

    override fun cloneBase(align: Align, offset: IntOffset, opacity: Float) = copy(
        align = align,
        offset = offset,
        opacity = opacity
    )

}