package net.nomia.common.data.model

import javax.measure.Unit

data class MeasureData(
    val value: Long,
    val presentedUnit: Unit<*>,
    val quantity: Quantity
) {
    enum class Quantity {
        Piece,
        Mass,
        Volume,
        Length,
        Area,
        Power,
        Time
    }
}
