package kr.hjkim.randombox

import kr.hjkim.library.objects.KimPlugin
import kr.hjkim.randombox.commands.RandomBoxCommand

class RandomBox: KimPlugin() {

    override fun onEnable() {
        registerCommands(RandomBoxCommand())
        registerEvents(RandomBoxListener())
    }

}