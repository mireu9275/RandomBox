package kr.hjkim.randombox

import kr.hjkim.randombox.managers.ItemManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class RandomBoxListener: Listener {

    @EventHandler
    fun onItemClick(event: PlayerInteractEvent) {
        ItemManager.getItem(if(event.hasItem()) event.item!! else return)?.onClick(event)
    }

}