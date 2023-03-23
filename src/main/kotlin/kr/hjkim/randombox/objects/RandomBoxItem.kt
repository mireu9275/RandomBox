package kr.hjkim.randombox.objects

import kr.hjkim.library.extensions.getStringNBT
import kr.hjkim.randombox.managers.RandomBoxManager
import kr.hjkim.randombox.objects.gui.RandomBoxGUI
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class RandomBoxItem: Item() {

    override fun onClick(event: PlayerInteractEvent) {
        val item = event.item!!
        val key = item.getStringNBT("randombox_key") ?: return
        val randomBox = RandomBoxManager.getRandomBox(key) ?: return
        val player = event.player
        if(!randomBox.canPlay()) {
            player.sendMessage("예기치 못한 오류가 발생했습니다. 관리자에게 문의하세요.")
            return
        }
        RandomBoxGUI(player,randomBox,item).firstOpen()
    }

}