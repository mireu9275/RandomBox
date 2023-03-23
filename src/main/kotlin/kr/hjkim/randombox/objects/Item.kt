package kr.hjkim.randombox.objects

import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

abstract class Item {

    abstract fun onClick(event: PlayerInteractEvent)

}