package top.fifthlight.touchcontroller.ui.view.config.category

import androidx.compose.runtime.Composable
import top.fifthlight.combine.data.Identifier
import top.fifthlight.combine.modifier.Modifier
import top.fifthlight.touchcontroller.ui.model.ConfigScreenViewModel

sealed class ConfigCategory(
    val title: Identifier,
    val content: @Composable (modifier: Modifier, viewModel: ConfigScreenViewModel) -> Unit,
)
