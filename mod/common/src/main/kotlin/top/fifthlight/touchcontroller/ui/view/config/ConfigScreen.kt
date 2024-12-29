package top.fifthlight.touchcontroller.ui.view.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import top.fifthlight.combine.data.Text
import top.fifthlight.combine.modifier.Modifier
import top.fifthlight.combine.modifier.placement.fillMaxWidth
import top.fifthlight.combine.widget.base.Text
import top.fifthlight.combine.widget.base.layout.Column
import top.fifthlight.combine.widget.ui.AlertDialog
import top.fifthlight.combine.widget.ui.Button
import top.fifthlight.combine.widget.ui.Tab
import top.fifthlight.combine.widget.ui.TabItem
import top.fifthlight.touchcontroller.ui.model.ConfigScreenViewModel
import top.fifthlight.touchcontroller.ui.view.config.category.ConfigCategory
import top.fifthlight.touchcontroller.ui.view.config.category.GlobalCategory
import top.fifthlight.touchcontroller.ui.view.config.category.ItemsCategory
import top.fifthlight.touchcontroller.ui.view.config.category.LayoutCategory

@Composable
private fun TabNavigationBar(
    modifier: Modifier = Modifier,
    categories: PersistentList<ConfigCategory> = persistentListOf(),
    selectedCategory: ConfigCategory? = null,
    onCategorySelected: (ConfigCategory) -> Unit = {}
) {
    Tab(modifier = modifier) {
        for (category in categories) {
            TabItem(
                selected = selectedCategory == category,
                onSelected = {
                    onCategorySelected(category)
                }
            ) {
                Text(
                    text = Text.translatable(category.title),
                    color = it
                )
            }
        }
    }
}

@Composable
fun ConfigScreen(viewModel: ConfigScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    if (uiState.showExitDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.dismissExitDialog()
            },
            title = {
                Text("Warning")
            },
            action = {
                Button(onClick = {
                    viewModel.exit()
                }) {
                    Text("Yes", shadow = true)
                }
                Button(onClick = {
                    viewModel.dismissExitDialog()
                }) {
                    Text("No", shadow = true)
                }
            },
        ) {
            Text("You have unsaved changes. Do you really want to exit? All unsaved changed will be lost.")
        }
    }
    Column {
        TabNavigationBar(
            modifier = Modifier.fillMaxWidth(),
            categories = persistentListOf(
                GlobalCategory,
                ItemsCategory,
                LayoutCategory,
            ),
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = { category ->
                viewModel.selectCategory(category)
            },
        )
        uiState.selectedCategory.content(
            Modifier
                .fillMaxWidth()
                .weight(1f),
            viewModel
        )
    }
}