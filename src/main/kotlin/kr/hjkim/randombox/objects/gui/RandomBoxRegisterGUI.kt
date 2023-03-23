package kr.hjkim.randombox.objects.gui

import kr.hjkim.library.managers.ItemStackUtil
import kr.hjkim.library.managers.SignPanelManager
import kr.hjkim.library.objects.GUI
import kr.hjkim.randombox.managers.RandomBoxManager
import kr.hjkim.randombox.objects.RandomBox
import kr.hjkim.randombox.objects.RandomItem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException

class RandomBoxRegisterGUI(player: Player): GUI(player, "랜덤박스 설정", 6) {

    private var page: Int = 0
    private lateinit var guiMode: GUIMode
    private lateinit var selectBox: RandomBox
    private val boxMap: HashMap<Int,RandomBox> = HashMap()

    private fun openCreateRandomBoxPanel() {
        SignPanelManager.createPanel(listOf("","적으세요","ㅁㄴㅇㄹ","- : 취소")) { player, lore ->
            val key = lore[0]
            if(key.isNotBlank() && key != "-") {
                if(RandomBoxManager.getRandomBox(key) != null) {
                    player.sendMessage("이미 해당 랜덤박스가 존재합니다.")
                    open()
                    return@createPanel
                }
                RandomBoxManager.addRandomBox(key)
                setFirstGUI()
                player.sendMessage("성공적으로 생성되었습니다.")
            }
            open()
        }.open(player)
    }

    private fun openEditRandomItemChancePanel(randomItem: RandomItem) {
        SignPanelManager.createPanel(listOf("","변경할 확률을","적어주세요.","- : 취소")) { player, lore ->
            val key = lore[0]
            if(key.isNotBlank() && key != "-") {
                val chance = try { key.toDouble() } catch (e: NumberFormatException) {
                    player.sendMessage("값은 소수 형태로 입력할 수 있습니다.")
                    open()
                    return@createPanel
                }
                randomItem.setChance(chance)
                setRandomBoxEditGUI(selectBox)
                if(selectBox.canPlay()) player.sendMessage("성공적으로 수정되었습니다.")
                else player.sendMessage("랜덤 아이템의 총 확률이 100이 아닙니다. 100 으로 맞추지 않을 경우, 랜덤박스를 열 수 없습니다.")
            }
            open()
        }.open(player)
    }

    private fun setRandomBoxEditGUI(randomBox: RandomBox) {
        clear()
        guiMode = GUIMode.EDIT_RANDOMBOX
        selectBox = randomBox
        setItem(EXIT_SLOT, exitIcon())
        var count = 9
        for(item in selectBox.getAllRandomItems()) {
            setItem(count++,item.getSetupIcon())
        }
    }

    private fun turnPage(isNext: Boolean) {
        if(guiMode != GUIMode.MAIN) return
        if(!isNext) {
            if(page <= 0) return
            page -= 1
        } else {
            if(RandomBoxManager.getAllRandomBoxes().toList().getOrNull((page + 1) * 45) == null) return
            page += 1
        }
        setFirstGUI()
    }

    override fun setFirstGUI() {
        clear()
        guiMode = GUIMode.MAIN
        setItem(EXIT_SLOT, exitIcon())
        setItem(CREATE_SLOT, createIcon())
        setItem(TURN_PREV_SLOT, turnPrevIcon())
        setItem(TURN_NEXT_SLOT, turnNextIcon())
        val iterator = RandomBoxManager.getAllRandomBoxes().toList().listIterator(page * 45)
        for(slot in 9 until size) {
            if(!iterator.hasNext()) break
            val randomBox = iterator.next()
            setItem(slot,randomBox.getRegisterIcon())
            boxMap[slot] = randomBox
        }
    }

    override fun InventoryClickEvent.clickEvent() {
        isCancelled = true
        when(rawSlot) {
            EXIT_SLOT -> {
                when(guiMode) {
                    GUIMode.MAIN -> player.closeInventory()
                    GUIMode.EDIT_RANDOMBOX -> setFirstGUI()
                }
            }
            CREATE_SLOT -> {
                if(guiMode == GUIMode.MAIN) openCreateRandomBoxPanel()
            }
            TURN_PREV_SLOT -> turnPage(false)
            TURN_NEXT_SLOT -> turnPage(true)
            else -> {
                when(guiMode) {
                    GUIMode.MAIN -> {
                        val randomBox = boxMap[rawSlot] ?: return
                        when(click) {
                            ClickType.LEFT -> setRandomBoxEditGUI(randomBox)
                            ClickType.RIGHT -> player.inventory.addItem(randomBox.getRandomBoxItem())
                            ClickType.SHIFT_RIGHT -> {
                                page = 0
                                RandomBoxManager.removeRandomBox(randomBox.key)
                                setFirstGUI()
                            }
                            else -> return
                        }
                    }
                    GUIMode.EDIT_RANDOMBOX -> {
                        if(rawSlot >= size) {
                            val item = currentItem
                            if(item == null || item.type == Material.AIR) return
                            selectBox.addRandomItem(item.clone())
                            setRandomBoxEditGUI(selectBox)
                        } else {
                            val randomItem = try { selectBox.getAllRandomItems()[rawSlot - 9] } catch (e: IndexOutOfBoundsException) { return }
                            when(click) {
                                ClickType.LEFT, ClickType.SHIFT_LEFT -> openEditRandomItemChancePanel(randomItem)
                                ClickType.RIGHT, ClickType.SHIFT_RIGHT -> {
                                    selectBox.removeRandomItem(randomItem)
                                    setRandomBoxEditGUI(selectBox)
                                }
                                else -> return
                            }
                        }
                    }
                }
            }
        }
    }
    override fun InventoryCloseEvent.closeEvent() {}
    override fun InventoryDragEvent.dragEvent() {}

    companion object {
        private enum class GUIMode {
            MAIN,
            EDIT_RANDOMBOX
        }

        private const val EXIT_SLOT: Int = 8
        private const val CREATE_SLOT: Int = 0
        private const val TURN_PREV_SLOT: Int = 6
        private const val TURN_NEXT_SLOT: Int = 7

        private fun exitIcon(): ItemStack = ItemStackUtil.build(Material.BARRIER) { meta -> meta.setDisplayName("§c나가기") }
        private fun createIcon(): ItemStack = ItemStackUtil.build(Material.CHEST) { meta -> meta.setDisplayName("§a추가"); meta.lore = listOf("§7새로운 랜덤박스를 추가합니다.") }
        private fun turnPrevIcon(): ItemStack = ItemStackUtil.build(Material.PAPER) { meta -> meta.setDisplayName("§f이전 페이지") }
        private fun turnNextIcon(): ItemStack = ItemStackUtil.build(Material.PAPER) { meta -> meta.setDisplayName("§f다음 페이지") }

    }

}