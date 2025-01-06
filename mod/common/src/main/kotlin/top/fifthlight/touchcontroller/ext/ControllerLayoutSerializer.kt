package top.fifthlight.touchcontroller.ext

import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import top.fifthlight.touchcontroller.config.ControllerLayout
import top.fifthlight.touchcontroller.config.LayoutLayer
import top.fifthlight.touchcontroller.control.ControllerWidget

class ControllerLayoutSerializer : KSerializer<ControllerLayout> {
    private class PersistentListDescriptor : SerialDescriptor by serialDescriptor<List<ControllerWidget>>() {
        @OptIn(ExperimentalSerializationApi::class)
        override val serialName: String = "top.fifthlight.touchcontroller.config.ControllerLayout"
    }

    private val itemSerializer = serializer<LayoutLayer>()
    private val delegatedSerializer = ListSerializer(itemSerializer)

    override val descriptor: SerialDescriptor = PersistentListDescriptor()

    override fun serialize(encoder: Encoder, value: ControllerLayout) =
        delegatedSerializer.serialize(encoder, value.layers)

    override fun deserialize(decoder: Decoder) =
        ControllerLayout(delegatedSerializer.deserialize(decoder).toPersistentList())
}
