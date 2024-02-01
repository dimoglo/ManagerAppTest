package net.nomia.pos.core.text

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class Content {

    abstract fun stringValue(resources: Resources) : String

    object Empty : Content() {
        override fun stringValue(resources: Resources) = ""
    }

    data class Text(val text: String) : Content() {
        override fun stringValue(resources: Resources) = text
    }

    data class ResValue(@StringRes val stringRes: Int, val args: List<Any> = listOf()) : Content() {

        constructor(@StringRes stringRes: Int, arg: Any) : this(stringRes, listOf(arg))

        override fun stringValue(resources: Resources) = resources.getString(stringRes, *args.toTypedArray())
    }

    data class PluralValue(
        @PluralsRes val pluralRes: Int,
        val quantity: Int,
        val args: List<Any> = listOf()
    ) : Content() {

        constructor(@PluralsRes pluralRes: Int, quantity: Int, arg: Any) : this(pluralRes, quantity, listOf(arg))

        override fun stringValue(resources: Resources) =
            resources.getQuantityString(pluralRes, quantity, *args.toTypedArray())
    }


    data class Composite(@StringRes val stringRes: Int, val args: List<Content>) : Content() {

        constructor(@StringRes stringRes: Int, content: Content) : this(stringRes, listOf(content))

        override fun stringValue(resources: Resources): String {
            val contentValues = args.map { content -> content.stringValue(resources) }
            return resources.getString(stringRes, *contentValues.toTypedArray())
        }
    }

    data class Joined(val contents: List<Content>, val separator: String = " ") : Content() {
        override fun stringValue(resources: Resources): String =
            contents.joinToString(separator) { content -> content.stringValue(resources) }
    }

}
