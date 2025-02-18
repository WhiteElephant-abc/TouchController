package top.fifthlight.combine.platform

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import net.minecraft.item.ArmorItem
import net.minecraft.item.BundleItem
import net.minecraft.item.Equipment
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.RangedWeaponItem
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import top.fifthlight.combine.data.Identifier
import top.fifthlight.combine.data.ItemFactory
import top.fifthlight.combine.data.ItemSubclass
import kotlin.jvm.optionals.getOrNull
import top.fifthlight.combine.data.Item as CombineItem
import top.fifthlight.combine.data.ItemStack as CombineItemStack

object ItemFactoryImpl : ItemFactory {
    override fun createItem(id: Identifier): CombineItem? {
        val item = Registries.ITEM.getOrEmpty(id.toMinecraft()).getOrNull() ?: return null
        return ItemImpl(item)
    }

    override fun createItemStack(
        item: CombineItem,
        amount: Int
    ): CombineItemStack {
        val minecraftItem = (item as ItemImpl).inner
        val stack = ItemStack(minecraftItem, amount)
        return ItemStackImpl(stack)
    }

    override fun createItemStack(id: Identifier, amount: Int): CombineItemStack? {
        val item = Registries.ITEM.getOrEmpty(id.toMinecraft()).getOrNull() ?: return null
        val stack = ItemStack(item, amount)
        return ItemStackImpl(stack)
    }

    override val allItems: PersistentList<CombineItem> by lazy {
        Registries.ITEM.map(Item::toCombine).toPersistentList()
    }

    val rangedWeaponSubclass = ItemSubclassImpl(
        name = TextImpl(Text.literal("Ranged weapon")),
        configId = "RangedWeaponItem",
        clazz = RangedWeaponItem::class.java
    )

    val armorSubclass = ItemSubclassImpl(
        name = TextImpl(Text.literal("Armor")),
        configId = "ArmorItem",
        clazz = ArmorItem::class.java
    )

    val equipmentSubclass = ItemSubclassImpl(
        name = TextImpl(Text.literal("Equipment")),
        configId = "Equipment",
        clazz = Equipment::class.java
    )

    val bundleSubclass = ItemSubclassImpl(
        name = TextImpl(Text.literal("Bundle")),
        configId = "BundleItem",
        clazz = BundleItem::class.java
    )

    override val subclasses: PersistentList<ItemSubclass> = persistentListOf(
        rangedWeaponSubclass,
        armorSubclass,
        equipmentSubclass,
        bundleSubclass,
    )
}

fun Item.toCombine() = ItemImpl(this)
fun ItemStack.toCombine() = ItemStackImpl(this)
fun CombineItem.toVanilla() = (this as ItemImpl).inner
fun CombineItemStack.toVanilla() = (this as ItemStackImpl).inner
