package net.nomia.common.ui.extensions

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

fun TextFieldValue.trim(vararg trimmedChars : Char) : TextFieldValue {
    val textToTrim = text
    val trimmedText = StringBuilder()
    var selectionStart = selection.start
    var selectionEnd = selection.end
    textToTrim.forEachIndexed { index, c ->
        if (c in trimmedChars) {
            val shiftedIndex = index + 1
            if (selection.start <= shiftedIndex) { selectionStart-- }
            if (selection.end <= shiftedIndex) { selectionEnd-- }
        } else {
            trimmedText.append(c)
        }
    }
    return copy(text = trimmedText.toString(), selection = TextRange(selectionStart, selectionEnd))
}

fun TextFieldValue.replacePrefix(prefix : String, replacement: String) : TextFieldValue {
    return if (text.startsWith(prefix)) {
        val text = replacement + text.removePrefix(prefix)
        val shift = prefix.length - replacement.length
        val selection = selection.run { TextRange(start - shift , end - shift) }
        copy(text = text, selection = selection)
    } else {
        this
    }
}
