package net.nomia.common.data

import net.nomia.common.data.model.Amount
import net.nomia.common.data.model.DefaultUnits
import net.nomia.common.data.model.Measure
import net.nomia.common.data.model.MeasureData
import tech.units.indriya.AbstractUnit
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units
import java.math.BigDecimal
import javax.measure.Unit
import javax.measure.quantity.Area
import javax.measure.quantity.Length
import javax.measure.quantity.Mass
import javax.measure.quantity.Power
import javax.measure.quantity.Time
import javax.measure.quantity.Volume

fun Amount.toCurrencyAdjusted(): BigDecimal {
    val currencyFormatter = CurrencyHelper.getCurrencyFormatter(currency)
    return value.setScale(currencyFormatter.maximumFractionDigits, currencyFormatter.roundingMode)
}

fun Measure.toData() = when (this) {
    is Measure.Area -> MeasureData(
        value = value.to(DefaultUnits.SQUARE_CENTIMETER).value.toLong(),
        presentedUnit = value.unit,
        quantity = MeasureData.Quantity.Area
    )
    is Measure.Length -> MeasureData(
        value = value.to(DefaultUnits.CENTIMETER).value.toLong(),
        presentedUnit = value.unit,
        quantity = MeasureData.Quantity.Length
    )
    is Measure.Mass -> MeasureData(
        value = value.to(Units.GRAM).value.toLong(),
        presentedUnit = value.unit,
        quantity = MeasureData.Quantity.Mass
    )
    is Measure.Piece -> MeasureData(
        value = value.value.toLong(),
        presentedUnit = value.unit,
        quantity = MeasureData.Quantity.Piece
    )
    is Measure.Power -> MeasureData(
        value = value.to(Units.WATT).value.toLong(),
        presentedUnit = value.unit,
        quantity = MeasureData.Quantity.Power
    )
    is Measure.Time -> MeasureData(
        value = value.to(Units.MINUTE).value.toLong(),
        presentedUnit = value.unit,
        quantity = MeasureData.Quantity.Time
    )
    is Measure.Volume -> MeasureData(
        value = value.to(DefaultUnits.MILLILITER).value.toLong(),
        presentedUnit = value.unit,
        quantity = MeasureData.Quantity.Volume
    )
}

@Suppress("UNCHECKED_CAST")
fun MeasureData.toDomain() = when (quantity) {
    MeasureData.Quantity.Piece -> Measure.Piece(Quantities.getQuantity(value, AbstractUnit.ONE))
    MeasureData.Quantity.Mass -> Measure.Mass(
        Quantities.getQuantity(value, Units.GRAM).to(presentedUnit as Unit<Mass>)
    )
    MeasureData.Quantity.Volume -> Measure.Volume(
        Quantities.getQuantity(
            value,
            DefaultUnits.MILLILITER
        ).to(presentedUnit as Unit<Volume>)
    )
    MeasureData.Quantity.Length -> Measure.Length(
        Quantities.getQuantity(
            value,
            DefaultUnits.CENTIMETER
        ).to(presentedUnit as Unit<Length>)
    )
    MeasureData.Quantity.Area -> Measure.Area(
        Quantities.getQuantity(
            value,
            DefaultUnits.SQUARE_CENTIMETER
        ).to(presentedUnit as Unit<Area>)
    )
    MeasureData.Quantity.Power -> Measure.Power(
        Quantities.getQuantity(value, Units.WATT).to(presentedUnit as Unit<Power>)
    )
    MeasureData.Quantity.Time -> Measure.Time(
        Quantities.getQuantity(value, Units.MINUTE).to(presentedUnit as Unit<Time>)
    )
}
