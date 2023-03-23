package kr.hjkim.randombox.managers

import kr.hjkim.library.extensions.getStringNBT
import kr.hjkim.randombox.enums.ItemType
import kr.hjkim.randombox.objects.HatItem
import kr.hjkim.randombox.objects.Item
import kr.hjkim.randombox.objects.RandomBoxItem
import org.bukkit.inventory.ItemStack

object ItemManager {

    private val itemMap: HashMap<String,Item> = HashMap()

    fun getItem(key: String): Item? = itemMap[key]

    fun getItem(itemStack: ItemStack): Item? { return getItem(itemStack.getStringNBT("ITEM_KEY") ?: return null) }

    init {
        itemMap["randombox"] = RandomBoxItem()
        itemMap["hat"] = HatItem()
    }

}