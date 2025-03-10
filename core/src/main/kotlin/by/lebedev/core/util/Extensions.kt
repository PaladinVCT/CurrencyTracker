package by.lebedev.core.util

import android.content.Context
import android.util.Base64
import android.widget.Toast



object Extensions {
    fun Context.myToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun encodeToBase64(input: String?): String? {
    if (input == null) {
        return null
    }

    return try {
        val bytes = input.toByteArray(Charsets.UTF_8)
        Base64.encodeToString(bytes, Base64.DEFAULT)
    } catch (e: Exception) {
        null
    }
}

fun decodeFromBase64(input: String?): String? {
    if (input == null) {
        return null
    }

    return try {
        val bytes = Base64.decode(input, Base64.DEFAULT)
        String(bytes, Charsets.UTF_8)
    } catch (e: IllegalArgumentException) {
        null
    } catch (e: Exception) {
        null
    }
}