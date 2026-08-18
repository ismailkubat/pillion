package app.pillion.core

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual object Logger {
    actual fun d(message: String) {
        Log.d(TAG, message)
        LogBuffer.append("${timestamp()} D $message")
    }
    actual fun e(message: String, error: Throwable?) {
        Log.e(TAG, message, error)
        LogBuffer.append("${timestamp()} E $message" + (error?.let { " — $it" } ?: ""))
    }
    private const val TAG = "Pillion"

    // A fresh SimpleDateFormat per call: the class isn't thread-safe, and Logger.d/e is called
    // from several threads at once (mirror loop, foreground service, broadcast receivers) — a
    // single shared instance intermittently threw/mangled timestamps under real concurrent use.
    private fun timestamp(): String = SimpleDateFormat("HH:mm:ss.SSS", Locale.US).format(Date())
}