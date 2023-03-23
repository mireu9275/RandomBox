package kr.hjkim.randombox.objects

import kr.hjkim.library.extensions.setStringNBT
import kr.hjkim.library.managers.ItemStackUtil
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class RandomBox(val key: String) {

    private val items: ArrayList<RandomItem> = ArrayList()

    fun canPlay(): Boolean {
        if(items.isEmpty()) return false
        var chance = 0
        for(item in items) chance += item.chance
        return chance == 100000
    }

    fun getAllRandomItems(): ArrayList<out RandomItem> = items

    fun addRandomItem(itemStack: ItemStack) { items.add(RandomItem(itemStack))}

    fun removeRandomItem(randomItem: RandomItem) { items.remove(randomItem) }

    fun getRandomItem(): RandomItem {
        val sortedList = items.sortedBy { it.chance }
        val random = (1..100000).random()
        var previousChance = 1
        for(item in sortedList) {
            previousChance += item.chance
            if(random <= previousChance) return item
        }
        return sortedList.last()
    }

    fun getRandomBoxItem(): ItemStack {
        return ItemStackUtil.build(Material.ENDER_CHEST) { meta ->
            meta.setDisplayName("랜덤박스 #$key")
            val lore = ArrayList<String>()
            val iterator = items.iterator()
            for(i in 0 until 5) {
                if(!iterator.hasNext()) break
                val item = iterator.next().itemStack
                val itemMeta = item.itemMeta!!
                val name = if(itemMeta.hasDisplayName()) itemMeta.displayName else item.type.name
                val amount = item.amount
                val format = "§f$name : §e$amount §f개"
                lore.add(format)
            }
            meta.setStringNBT("ITEM_KEY","randombox")
            meta.setStringNBT("randombox_key",key)
            meta.lore = lore
        }
    }

    fun getRegisterIcon(): ItemStack {
        return ItemStackUtil.build(Material.ENDER_CHEST) { meta ->
            meta.setDisplayName("랜덤박스 #$key")
            meta.lore = listOf(
                "좌클릭 : 수정",
                "우클릭 : 지급",
                "쉬프트 + 우클릭 : 삭제"
            )
        }
    }

    fun save() {

    }

}