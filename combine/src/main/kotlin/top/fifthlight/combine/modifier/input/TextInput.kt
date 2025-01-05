package top.fifthlight.combine.modifier.input

import top.fifthlight.combine.input.input.TextInputReceiver
import top.fifthlight.combine.modifier.Modifier
import top.fifthlight.combine.modifier.TextInputModifierNode

fun Modifier.textInput(handler: TextInputReceiver) = then(TextInputReceiverModifierNode(handler))

private data class TextInputReceiverModifierNode(
    val handler: TextInputReceiver
) : Modifier.Node<TextInputReceiverModifierNode>, TextInputModifierNode {
    override fun onTextInput(string: String) = handler.onTextInput(string)
}