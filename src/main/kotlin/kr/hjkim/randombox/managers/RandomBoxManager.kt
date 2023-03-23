package kr.hjkim.randombox.managers

import kr.hjkim.randombox.objects.RandomBox

object RandomBoxManager {

    private val boxMap: HashMap<String, RandomBox> = HashMap()

    fun getRandomBox(key: String): RandomBox? = boxMap[key]

    fun getAllRandomBoxes(): MutableCollection<out RandomBox> = boxMap.values

    fun addRandomBox(key: String) { boxMap[key] = RandomBox(key) }

    fun removeRandomBox(key: String) { boxMap.remove(key) }

}