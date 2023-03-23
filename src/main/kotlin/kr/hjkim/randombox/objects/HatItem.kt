package kr.hjkim.randombox.objects

import org.bukkit.event.player.PlayerInteractEvent

class HatItem: Item() {

    override fun onClick(event: PlayerInteractEvent) {
        val item = event.item!!
        event.player.inventory.helmet = item
        event.player.inventory.setItemInMainHand(null)
    }

}