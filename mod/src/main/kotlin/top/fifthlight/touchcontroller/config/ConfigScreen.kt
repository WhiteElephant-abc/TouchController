package top.fifthlight.touchcontroller.config

import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import dev.isxander.yacl3.dsl.textSwitch
import kotlinx.collections.immutable.toPersistentList
import net.minecraft.client.gui.screen.Screen
import net.minecraft.item.Item
import org.koin.core.context.GlobalContext
import top.fifthlight.touchcontroller.TouchController
import top.fifthlight.touchcontroller.asset.Texts
import top.fifthlight.touchcontroller.config.widget.ItemsListController
import top.fifthlight.touchcontroller.ext.ItemsList

fun openConfigScreen(parent: Screen): Screen {
    val context = GlobalContext.get()
    val configHolder: TouchControllerConfigHolder = context.get()
    var config = configHolder.config.value

    return YetAnotherConfigLib(TouchController.NAMESPACE) {
        title(Texts.OPTIONS_SCREEN_TITLE)

        val globalCategory by categories.registering("global") {
            name(Texts.OPTIONS_CATEGORY_GLOBAL_TITLE)
            tooltip(Texts.OPTIONS_CATEGORY_GLOBAL_TOOLTIP)

            val disableMouse by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_DESCRIPTION))
                controller(textSwitch())
                binding(true, { config.disableMouse }, { config = config.copy(disableMouse = it) })
            }

            val disableMouseLock by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_LOCK_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_GLOBAL_DISABLE_MOUSE_LOCK_DESCRIPTION))
                controller(textSwitch())
                binding(true, { config.disableMouseLock }, { config = config.copy(disableMouseLock = it) })
            }

            val disableCrosshair by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_GLOBAL_DISABLE_CROSSHAIR_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_GLOBAL_DISABLE_CROSSHAIR_DESCRIPTION))
                controller(textSwitch())
                binding(true, { config.disableCrosshair }, { config = config.copy(disableCrosshair = it) })
            }

            val showPointers by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_GLOBAL_SHOW_POINTERS_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_GLOBAL_SHOW_POINTERS_DESCRIPTION))
                controller(textSwitch())
                binding(false, { config.showPointers }, { config = config.copy(showPointers = it) })
            }

            val enableTouchEmulation by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_GLOBAL_ENABLE_TOUCH_EMULATION_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_GLOBAL_ENABLE_TOUCH_EMULATION_DESCRIPTION))
                controller(textSwitch())
                binding(false, { config.enableTouchEmulation }, { config = config.copy(enableTouchEmulation = it) })
            }
        }

        val itemsCategory by categories.registering("items") {
            name(Texts.OPTIONS_CATEGORY_ITEMS_TITLE)
            tooltip(Texts.OPTIONS_CATEGORY_ITEMS_TOOLTIP)

            val usableItems by rootOptions.registering<List<Item>> {
                name(Texts.OPTIONS_CATEGORY_ITEMS_USABLE_ITEMS_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_ITEMS_USABLE_ITEMS_DESCRIPTION))
                customController { ItemsListController(it) }
                binding(
                    defaultUsableItems,
                    { config.usableItems.items },
                    { config = config.copy(usableItems = ItemsList(it.toPersistentList())) }
                )
            }

            val projectileShowCrosshair by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_ITEMS_PROJECTILE_SHOW_CROSSHAIR_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_ITEMS_PROJECTILE_SHOW_CROSSHAIR_DESCRIPTION))
                controller(textSwitch())
                binding(true, { config.projectileShowCrosshair }, { config = config.copy(projectileShowCrosshair = it) })
            }

            val rangedWeaponShowCrosshair by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_ITEMS_RANGED_WEAPONS_SHOW_CROSSHAIR_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_ITEMS_RANGED_WEAPONS_SHOW_CROSSHAIR_DESCRIPTION))
                controller(textSwitch())
                binding(true, { config.rangedWeaponShowCrosshair }, { config = config.copy(rangedWeaponShowCrosshair = it) })
            }

            val foodUsable by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_ITEMS_FOOD_USABLE_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_ITEMS_FOOD_USABLE_DESCRIPTION))
                controller(textSwitch())
                binding(true, { config.foodUsable }, { config = config.copy(foodUsable = it) })
            }

            val projectileUsable by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_ITEMS_PROJECTILE_USABLE_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_ITEMS_PROJECTILE_USABLE_DESCRIPTION))
                controller(textSwitch())
                binding(true, { config.projectileUsable }, { config = config.copy(projectileUsable = it) })
            }

            val rangedWeaponUsable by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_ITEMS_RANGED_WEAPONS_USABLE_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_ITEMS_RANGED_WEAPONS_USABLE_DESCRIPTION))
                controller(textSwitch())
                binding(true, { config.rangedWeaponUsable }, { config = config.copy(rangedWeaponUsable = it) })
            }

            val equippableUsable by rootOptions.registering {
                name(Texts.OPTIONS_CATEGORY_ITEMS_EQUIPPABLE_ITEMS_USABLE_TITLE)
                description(OptionDescription.of(Texts.OPTIONS_CATEGORY_ITEMS_EQUIPPABLE_ITEMS_USABLE_DESCRIPTION))
                controller(textSwitch())
                binding(true, { config.equippableUsable }, { config = config.copy(equippableUsable = it) })
            }
        }

        categories.register(
            "custom", CustomCategory(
                name = Texts.OPTIONS_CATEGORY_CUSTOM_TITLE,
                tooltip = Texts.OPTIONS_CATEGORY_CUSTOM_TOOLTIP,
            )
        )

        save {
            configHolder.saveConfig(config)
        }
    }.generateScreen(parent)
}