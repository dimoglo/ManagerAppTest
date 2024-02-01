package net.nomia.erp.schema

import com.apollographql.apollo.api.Input
import net.nomia.common.data.model.PageRequest
import net.nomia.erp.schema.type.PageRequestInput
import net.nomia.erp.schema.type.SortDirection
import net.nomia.erp.schema.type.SortOrderInput
import java.time.DayOfWeek
import net.nomia.erp.schema.type.DayOfWeek as ApiDayOfWeek


fun PageRequest.toApi() = PageRequestInput(
    page = page,
    size = size.toInt(),
    sort = when {
        sort.isEmpty() -> Input.absent()
        else -> Input.optional(sort.map { it.toApi() })
    }
)

fun PageRequest.SortOrder.toApi() = SortOrderInput(
    fieldName = fieldName,
    direction = Input.optional(direction.toApi())
)

fun PageRequest.SortDirection.toApi() = when (this) {
    PageRequest.SortDirection.Asc -> SortDirection.ASC
    PageRequest.SortDirection.Desc -> SortDirection.DESC
}

fun ApiDayOfWeek.toDomain() = when (this) {
    ApiDayOfWeek.MONDAY -> DayOfWeek.MONDAY
    ApiDayOfWeek.TUESDAY -> DayOfWeek.TUESDAY
    ApiDayOfWeek.WEDNESDAY -> DayOfWeek.WEDNESDAY
    ApiDayOfWeek.THURSDAY -> DayOfWeek.THURSDAY
    ApiDayOfWeek.FRIDAY -> DayOfWeek.FRIDAY
    ApiDayOfWeek.SATURDAY -> DayOfWeek.SATURDAY
    ApiDayOfWeek.SUNDAY -> DayOfWeek.SUNDAY
    else -> throw Exception("Unsupported type $this")
}
