package kr.hjkim.randombox.objects.gui

import kr.hjkim.library.objects.GUI
import kr.hjkim.randombox.Blockable
import kr.hjkim.randombox.objects.RandomBox
import net.minecraft.network.chat.IChatBaseComponent
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack

class RandomBoxGUI(
    player: Player,
    private val randomBox: RandomBox,
    private val boxItem: ItemStack
): GUI(player,"§f\uF80A\uF809\u0FE8",6), Blockable {

    fun setTitle(title: String) {
        val entityPlayer = (player as CraftPlayer).handle
        val container = entityPlayer.bU
        val windowPacket = PacketPlayOutOpenWindow(container.j,container.a(),IChatBaseComponent.a(title))
        entityPlayer.b.a(windowPacket)
        player.updateInventory()
    }

    override fun setFirstGUI() {

    }

    override var isLocked: Boolean = false

    override fun InventoryClickEvent.clickEvent() {
        isCancelled = true
        if(boxItem.amount <= 0) return
        boxItem.amount -= 1
        asyncBlocking {
            setTitle("§f\uF80A\uF809\u0FE9")
            val randomItem = randomBox.getRandomItem()
            setItem(22,randomItem.itemStack)
        }
    }
    override fun InventoryCloseEvent.closeEvent() {}
    override fun InventoryDragEvent.dragEvent() {}

}