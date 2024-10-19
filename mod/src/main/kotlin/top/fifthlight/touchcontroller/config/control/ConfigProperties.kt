package top.fifthlight.touchcontroller.config.control

import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.CheckboxWidget
import net.minecraft.text.Text
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import top.fifthlight.touchcontroller.config.widget.ConfigSliderWidget
import top.fifthlight.touchcontroller.ext.setDimensions
import top.fifthlight.touchcontroller.proxy.data.IntSize

class BooleanProperty<Config : ControllerWidgetConfig>(
    private val getValue: (Config) -> Boolean,
    private val setValue: (Config, Boolean) -> Config,
    private val message: Text
) : ControllerWidgetConfig.Property<Config, Boolean, CheckboxWidget>, KoinComponent {
    override fun createController(editProvider: ControllerWidgetConfig.PropertyEditProvider<Config>) =
        object : ControllerWidgetConfig.PropertyWidget<Config, CheckboxWidget> {
            override fun createWidget(initialConfig: Config, size: IntSize) =
                CheckboxWidget
                    .builder(message, get())
                    .checked(getValue(initialConfig))
                    .callback { _, checked -> editProvider.newConfig(setValue(editProvider.currentConfig, checked)) }
                    .build().apply {
                        setDimensions(size)
                    }

            override fun updateWidget(config: Config, widget: CheckboxWidget) {
                val value = getValue(config)
                if (widget.isChecked != value) {
                    widget.onPress()
                }
            }
        }

}

class EnumProperty<Config : ControllerWidgetConfig, T>(
    private val getValue: (Config) -> T,
    private val setValue: (Config, T) -> Config,
    private val items: List<Pair<T, Text>>,
) : ControllerWidgetConfig.Property<Config, T, ButtonWidget> {
    private fun getItemText(item: T): Text =
        items.firstOrNull { it.first == item }?.second ?: Text.literal(item.toString())

    override fun createController(editProvider: ControllerWidgetConfig.PropertyEditProvider<Config>) =
        object : ControllerWidgetConfig.PropertyWidget<Config, ButtonWidget> {
            override fun createWidget(initialConfig: Config, size: IntSize): ButtonWidget {
                return ButtonWidget
                    .builder(getItemText(getValue(initialConfig))) {
                        if (items.isEmpty()) {
                            return@builder
                        }
                        val currentConfig = editProvider.currentConfig
                        val current = getValue(currentConfig)
                        val index = (items.indexOfFirst { it.first == current } + 1) % items.size
                        editProvider.newConfig(setValue(currentConfig, items[index].first))
                    }
                    .dimensions(0, 0, size.width, size.height)
                    .build()
            }

            override fun updateWidget(config: Config, widget: ButtonWidget) {
                widget.message = getItemText(getValue(config))
            }
        }
}

class FloatProperty<Config : ControllerWidgetConfig>(
    private val getValue: (Config) -> Float,
    private val setValue: (Config, Float) -> Config,
    private val startValue: Float = 0f,
    private val endValue: Float = 1f,
    private val messageFormatter: (Float) -> Text,
) : ControllerWidgetConfig.Property<Config, Float, ConfigSliderWidget> {
    init {
        require(endValue >= startValue)
    }

    private fun fromRawToValue(raw: Double): Float = (raw * (endValue - startValue) + startValue).toFloat()
    private fun fromValueToRaw(value: Float): Double = (value.toDouble() - startValue) / (endValue - startValue)
    override fun createController(editProvider: ControllerWidgetConfig.PropertyEditProvider<Config>) =
        object : ControllerWidgetConfig.PropertyWidget<Config, ConfigSliderWidget> {
            override fun createWidget(initialConfig: Config, size: IntSize) = ConfigSliderWidget(
                width = size.width,
                height = size.height,
                messageFormatter = { messageFormatter(fromRawToValue(it)) },
                onValueChanged = { _, newValue ->
                    editProvider.newConfig(
                        setValue(
                            editProvider.currentConfig,
                            fromRawToValue(newValue)
                        )
                    )
                },
                value = fromValueToRaw(getValue(initialConfig))
            )

            override fun updateWidget(config: Config, widget: ConfigSliderWidget) =
                widget.setValue(fromValueToRaw(getValue(config)))
        }
}

class IntProperty<Config : ControllerWidgetConfig>(
    private val getValue: (Config) -> Int,
    private val setValue: (Config, Int) -> Config,
    private val range: IntRange,
    private val messageFormatter: (Int) -> Text,
) : ControllerWidgetConfig.Property<Config, Int, ConfigSliderWidget> {
    private fun fromRawToValue(raw: Double): Int = (raw * (range.last - range.first + 1) + range.first).toInt()
    private fun fromValueToRaw(value: Int): Double = (value.toDouble() - range.first) / (range.last - range.first + 1)

    override fun createController(editProvider: ControllerWidgetConfig.PropertyEditProvider<Config>) =
        object : ControllerWidgetConfig.PropertyWidget<Config, ConfigSliderWidget> {
            override fun createWidget(initialConfig: Config, size: IntSize) = ConfigSliderWidget(
                width = size.width,
                height = size.height,
                messageFormatter = { messageFormatter(fromRawToValue(it)) },
                onValueChanged = { _, newValue ->
                    editProvider.newConfig(
                        setValue(
                            editProvider.currentConfig,
                            fromRawToValue(newValue)
                        )
                    )
                },
                value = fromValueToRaw(getValue(initialConfig))
            )

            override fun updateWidget(config: Config, widget: ConfigSliderWidget) =
                widget.setValue(fromValueToRaw(getValue(config)))
        }
}