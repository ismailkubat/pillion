package app.pillion.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Minimal multiplatform logger (Android: logcat tag "Pillion"). */
expect object Logger {
    fun d(message: String)
    fun e(message: String, error: Throwable? = null)
}

/**
 * Ring buffer of recent log lines, mirrored from [Logger]. Lets the app show its own protocol
 * trace on-device (Settings -> Diagnostics) — no adb/USB/same-Wi-Fi needed to see what happened
 * on a ride, which matters most exactly when something went wrong away from a computer.
 *
 * [append] can be called from several threads at once (mirror loop, background services), so the
 * read-modify-write on [_lines] goes through [MutableStateFlow.update] (CAS retry) instead of a
 * plain get-then-set, which would silently drop lines under concurrent writes.
 */
object LogBuffer {
    private const val MAX_LINES = 400
    private val _lines = MutableStateFlow<List<String>>(emptyList())
    val lines: StateFlow<List<String>> = _lines

    fun append(line: String) {
        _lines.update { current ->
            val next = current + line
            if (next.size > MAX_LINES) next.takeLast(MAX_LINES) else next
        }
    }

    fun clear() {
        _lines.value = emptyList()
    }
}
