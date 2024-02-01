package net.nomia.common.data.model

import tech.units.indriya.unit.Units

class DefaultUnits {
    companion object {
        val MILLILITER = Units.LITRE.divide(1000.toDouble())
        val SQUARE_CENTIMETER = Units.SQUARE_METRE.divide(100.toDouble())
        val SQUARE_DECIMETER = Units.SQUARE_METRE.divide(10.toDouble())
        val CENTIMETER = Units.METRE.divide(100.toDouble())
        val KILOWATT = Units.WATT.divide(1000.toDouble())
        val TON = Units.KILOGRAM.multiply(1000.toDouble())
        val DECIMETER = Units.METRE.divide(10.toDouble())
    }
}
