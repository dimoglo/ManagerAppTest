package net.nomia.common.data.model

import tech.units.indriya.AbstractUnit
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units
import java.math.BigDecimal
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.Area
import javax.measure.quantity.Dimensionless
import javax.measure.quantity.Length
import javax.measure.quantity.Mass
import javax.measure.quantity.Power
import javax.measure.quantity.Time
import javax.measure.quantity.Volume

sealed interface Measure {
    val value: Quantity<*>

    data class Piece(override val value: Quantity<Dimensionless>) : Measure
    data class Mass(override val value: Quantity<javax.measure.quantity.Mass>) : Measure
    data class Volume(override val value: Quantity<javax.measure.quantity.Volume>) : Measure
    data class Length(override val value: Quantity<javax.measure.quantity.Length>) : Measure
    data class Area(override val value: Quantity<javax.measure.quantity.Area>) : Measure
    data class Power(override val value: Quantity<javax.measure.quantity.Power>) : Measure
    data class Time(override val value: Quantity<javax.measure.quantity.Time>) : Measure


    companion object {
        fun valueOf(unitType: UnitType, value: BigDecimal): Measure {
            return when (unitType) {
                UnitType.Gram -> Mass(Quantities.getQuantity(value, Units.GRAM))
                UnitType.Kilogram -> Mass(Quantities.getQuantity(value, Units.KILOGRAM))
                UnitType.Ton -> Mass(Quantities.getQuantity(value, DefaultUnits.TON))

                UnitType.Liter -> Volume(Quantities.getQuantity(value, Units.LITRE))
                UnitType.Milliliter -> Volume(
                    Quantities.getQuantity(
                        value,
                        DefaultUnits.MILLILITER
                    )
                )
                UnitType.CubicMeter -> Volume(Quantities.getQuantity(value, Units.CUBIC_METRE))

                UnitType.Centimeter -> Length(
                    Quantities.getQuantity(
                        value,
                        DefaultUnits.CENTIMETER
                    )
                )
                UnitType.Decimeter -> Length(Quantities.getQuantity(value, DefaultUnits.DECIMETER))
                UnitType.Meter -> Length(Quantities.getQuantity(value, Units.METRE))

                UnitType.SquareCentimeter -> Area(
                    Quantities.getQuantity(
                        value,
                        DefaultUnits.SQUARE_CENTIMETER
                    )
                )
                UnitType.SquareDecimeter -> Area(
                    Quantities.getQuantity(
                        value,
                        DefaultUnits.SQUARE_DECIMETER
                    )
                )
                UnitType.SquareMeter -> Area(Quantities.getQuantity(value, Units.SQUARE_METRE))

                UnitType.KilowattHour -> Power(Quantities.getQuantity(value, DefaultUnits.KILOWATT))
                UnitType.Day -> Time(Quantities.getQuantity(value, Units.DAY))
                UnitType.Hour -> Time(Quantities.getQuantity(value, Units.HOUR))
                UnitType.Minute -> Time(Quantities.getQuantity(value, Units.MINUTE))
                UnitType.Second -> Time(Quantities.getQuantity(value, Units.SECOND))

                UnitType.Piece -> Piece(Quantities.getQuantity(value, AbstractUnit.ONE))
            }
        }

        val singlePiece = Piece(Quantities.getQuantity(1, AbstractUnit.ONE))
    }
}

@Deprecated(
    message = "Use Measure.valueOf()",
    replaceWith = ReplaceWith("Measure.valueOf(unitType, BigDecimal.ZERO)", "java.math.BigDecimal")
)
fun zeroMeasureOf(unitType: UnitType): Measure = Measure.valueOf(unitType, BigDecimal.ZERO)

fun Measure.quantity() = value

fun Measure.value() = value.value.toDouble().toBigDecimal()

fun Measure.unitType(): UnitType = when (this.value.unit) {
    Units.GRAM -> UnitType.Gram
    Units.KILOGRAM -> UnitType.Kilogram
    DefaultUnits.TON -> UnitType.Ton

    Units.LITRE -> UnitType.Liter
    DefaultUnits.MILLILITER -> UnitType.Milliliter
    Units.CUBIC_METRE -> UnitType.CubicMeter

    DefaultUnits.CENTIMETER -> UnitType.Centimeter
    DefaultUnits.DECIMETER -> UnitType.Decimeter
    Units.METRE -> UnitType.Meter

    DefaultUnits.SQUARE_CENTIMETER -> UnitType.SquareCentimeter
    DefaultUnits.SQUARE_DECIMETER -> UnitType.SquareDecimeter
    Units.SQUARE_METRE -> UnitType.SquareMeter

    DefaultUnits.KILOWATT -> UnitType.KilowattHour
    Units.DAY -> UnitType.Day
    Units.HOUR -> UnitType.Hour
    Units.MINUTE -> UnitType.Minute
    Units.SECOND -> UnitType.Second

    AbstractUnit.ONE -> UnitType.Piece

    // TODO: elaborate exception
    else -> throw RuntimeException("Unsupported unit ${this.value.unit}")
}

fun Measure.copy(value: Number) = when (this) {
    is Measure.Piece -> this.copy(Quantities.getQuantity(value, this.value.unit))
    is Measure.Mass -> this.copy(Quantities.getQuantity(value, this.value.unit))
    is Measure.Volume -> this.copy(Quantities.getQuantity(value, this.value.unit))
    is Measure.Area -> this.copy(Quantities.getQuantity(value, this.value.unit))
    is Measure.Length -> this.copy(Quantities.getQuantity(value, this.value.unit))
    is Measure.Power -> this.copy(Quantities.getQuantity(value, this.value.unit))
    is Measure.Time -> this.copy(Quantities.getQuantity(value, this.value.unit))
}

@Suppress("UNCHECKED_CAST")
fun Measure.convertTo(unit: UnitType) = when (this) {
    is Measure.Area -> Measure.Area(value.to(unit.toUnit() as Unit<Area>))
    is Measure.Length -> Measure.Length(value.to(unit.toUnit() as Unit<Length>))
    is Measure.Mass -> Measure.Mass(value.to(unit.toUnit() as Unit<Mass>))
    is Measure.Piece -> this
    is Measure.Power -> Measure.Power(value.to(unit.toUnit() as Unit<Power>))
    is Measure.Time -> Measure.Time(value.to(unit.toUnit() as Unit<Time>))
    is Measure.Volume -> Measure.Volume(value.to(unit.toUnit() as Unit<Volume>))
}


fun UnitType.toUnit() = when (this) {
    UnitType.Gram -> Units.GRAM
    UnitType.Kilogram -> Units.KILOGRAM
    UnitType.Liter -> Units.LITRE
    UnitType.Milliliter -> DefaultUnits.MILLILITER
    UnitType.Piece -> AbstractUnit.ONE
    UnitType.Ton -> DefaultUnits.TON
    UnitType.Centimeter -> DefaultUnits.CENTIMETER
    UnitType.Decimeter -> DefaultUnits.DECIMETER
    UnitType.Meter -> Units.METRE
    UnitType.SquareCentimeter -> DefaultUnits.SQUARE_CENTIMETER
    UnitType.SquareDecimeter -> DefaultUnits.SQUARE_DECIMETER
    UnitType.SquareMeter -> Units.SQUARE_METRE
    UnitType.CubicMeter -> Units.CUBIC_METRE
    UnitType.KilowattHour -> DefaultUnits.KILOWATT
    UnitType.Day -> Units.DAY
    UnitType.Hour -> Units.HOUR
    UnitType.Minute -> Units.MINUTE
    UnitType.Second -> Units.SECOND
}
