package net.nomia.core.common.utils

data class PuzzleClicker(
    private var startTimestamp: Long = System.currentTimeMillis(),
    private var currentQuantity: Int = 0,
    private val hiddenNumber: Int = DEFAULT_HIDDEN_NUMBER,
) {

    constructor(quantityClicks: Int) : this(
        startTimestamp = System.currentTimeMillis(),
        currentQuantity = 0,
        hiddenNumber = quantityClicks
    )

    fun isSolved(): Boolean =
        System.currentTimeMillis().let { current ->
            current.takeIf { (current - startTimestamp) > TIME_DIFF_MILLISECONDS }
                ?.let {
                    currentQuantity = 1
                    false
                } ?: run { ++currentQuantity >= hiddenNumber }
        }.also { startTimestamp = System.currentTimeMillis() }

    companion object {
        const val DEFAULT_HIDDEN_NUMBER = 10
        const val TIME_DIFF_MILLISECONDS = 3000
    }
}
