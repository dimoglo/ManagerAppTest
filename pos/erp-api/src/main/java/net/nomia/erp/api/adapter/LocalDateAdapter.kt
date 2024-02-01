package net.nomia.erp.api.adapter

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import java.time.LocalDate

class LocalDateAdapter : CustomTypeAdapter<LocalDate> {
    override fun decode(value: CustomTypeValue<*>): LocalDate =
        LocalDate.parse((value.value as String))

    override fun encode(value: LocalDate): CustomTypeValue<*> =
        CustomTypeValue.GraphQLString(value.toString())
}
