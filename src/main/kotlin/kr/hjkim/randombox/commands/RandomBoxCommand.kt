package kr.hjkim.randombox.commands

import kr.hjkim.library.extensions.setStringNBT
import kr.hjkim.library.managers.ItemStackUtil
import kr.hjkim.library.objects.KimCommand
import kr.hjkim.randombox.objects.gui.RandomBoxRegisterGUI
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class RandomBoxCommand: KimCommand("랜덤박스") {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(args.isEmpty()) return true
        when(args[0]) {
            "test" -> RandomBoxRegisterGUI(sender as Player).firstOpen()
            "a" -> {
                val player = sender as Player
                player.inventory.addItem(
                    ItemStackUtil.build(Material.LIGHT_BLUE_STAINED_GLASS) { meta ->
                        meta.setStringNBT("ITEM_KEY","hat")
                    }
                )
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String> {
        //val arrayList = ArrayList<String>()
        //val list = listOf("Babo","HJKIM","RandomBox")
        //when(args.size) {
        //    1 -> {
        //        return StringUtil.copyPartialMatches(args[0],list,arrayList)
        //    }
        //}
        return emptyList()
    }

}