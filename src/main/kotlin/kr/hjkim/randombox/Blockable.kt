package kr.hjkim.randombox

import kr.hjkim.library.coroutine.async

interface Blockable {

    var isLocked: Boolean

    fun lock() { isLocked = true }
    fun unlock() { isLocked = false }

    fun asyncBlocking(block: () -> Unit) {
        if(isLocked) return
        lock()
        async {
            try { block() }
            finally { unlock() }
        }
    }

}