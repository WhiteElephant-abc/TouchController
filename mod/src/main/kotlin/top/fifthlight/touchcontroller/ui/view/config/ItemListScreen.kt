package top.fifthlight.touchcontroller.ui.view.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import top.fifthlight.combine.data.Item
import top.fifthlight.combine.layout.Alignment
import top.fifthlight.combine.layout.Arrangement
import top.fifthlight.combine.modifier.Modifier
import top.fifthlight.combine.modifier.drawing.border
import top.fifthlight.combine.modifier.placement.fillMaxWidth
import top.fifthlight.combine.modifier.placement.height
import top.fifthlight.combine.modifier.placement.padding
import top.fifthlight.combine.modifier.scroll.verticalScroll
import top.fifthlight.combine.paint.Colors
import top.fifthlight.combine.util.LocalCloseHandler
import top.fifthlight.combine.widget.base.Text
import top.fifthlight.combine.widget.base.layout.Box
import top.fifthlight.combine.widget.base.layout.Column
import top.fifthlight.combine.widget.base.layout.Row
import top.fifthlight.combine.widget.base.layout.Spacer
import top.fifthlight.combine.widget.ui.Button
import top.fifthlight.combine.widget.ui.Item
import top.fifthlight.touchcontroller.ui.component.AllItemGrid
import top.fifthlight.touchcontroller.ui.model.ItemListScreenViewModel

@Composable
private fun ItemList(
    modifier: Modifier = Modifier,
    value: PersistentList<Item> = persistentListOf(),
    onValueChanged: (PersistentList<Item>) -> Unit = {},
) {
    Column(
        modifier = modifier.verticalScroll()
    ) {
        value.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .padding(4)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Item(item = item)
                Text(text = item.name)
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    onValueChanged(value.removeAt(index))
                }) {
                    Text("Remove", shadow = true)
                }
            }
        }
    }
}

@Composable
fun ItemListScreen(viewModel: ItemListScreenViewModel) {
    Column {
        Box(
            modifier = Modifier
                .height(24)
                .fillMaxWidth()
                .border(bottom = 1, color = Colors.WHITE),
            alignment = Alignment.Center,
        ) {
            Text("Item list")
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            val uiState by viewModel.uiState.collectAsState()

            ItemList(
                modifier = Modifier.weight(1f),
                value = uiState.list,
                onValueChanged = {
                    viewModel.update(it)
                }
            )

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .height(32)
                        .fillMaxWidth(),
                    alignment = Alignment.Center
                ) {
                    Text("TODO search box")
                }

                AllItemGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onItemClicked = {
                        if (it !in uiState.list) {
                            viewModel.update(uiState.list + it)
                        }
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .height(32)
                .fillMaxWidth()
                .border(top = 1, color = Colors.WHITE),
            alignment = Alignment.Center,
        ) {
            val closeHandler = LocalCloseHandler.current
            Button(
                modifier = Modifier.fillMaxWidth(.25f),
                onClick = { viewModel.done(closeHandler) }
            ) {
                Text("Done", shadow = true)
            }
        }
    }
}