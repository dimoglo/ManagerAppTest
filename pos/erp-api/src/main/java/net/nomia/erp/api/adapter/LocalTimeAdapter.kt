package net.nomia.erp.api.adapter

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import java.time.LocalTime


class LocalTimeAdapter : CustomTypeAdapter<LocalTime> {
    override fun decode(value: CustomTypeValue<*>): LocalTime =
        LocalTime.parse((value.value as String))

    override fun encode(value: LocalTime): CustomTypeValue<*> =
        CustomTypeValue.GraphQLString(value.toString())
}
