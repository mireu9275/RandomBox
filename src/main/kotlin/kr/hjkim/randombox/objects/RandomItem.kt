package kr.hjkim.randombox.objects

import kr.hjkim.library.extensions.editItemMeta
import org.bukkit.inventory.ItemStack

class RandomItem(
    val itemStack: ItemStack,
    chance: Int = 0
) {

    var chance: Int = chance
        private set

    fun setChance(amount: Double) { this.chance = (amount * 1000).toInt() }

    fun getChanceFormat(): String = (chance.toDouble() / 1000).toString()

    fun getSetupIcon(): ItemStack {
        return itemStack.clone().editItemMeta { meta ->
            val lore = meta.lore ?: ArrayList<String>()
            lore.add("")
            lore.add("§a확률 : §f${getChanceFormat()} %")
            lore.add("")
            lore.add("§e좌클릭 : 확률 수정")
            lore.add("§e우클릭 : 삭제")
            meta.lore = lore
        }
    }

}