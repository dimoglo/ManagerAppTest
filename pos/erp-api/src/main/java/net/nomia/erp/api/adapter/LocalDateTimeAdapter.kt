package net.nomia.erp.api.adapter

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import java.time.LocalDateTime

class LocalDateTimeAdapter : CustomTypeAdapter<LocalDateTime> {
    override fun decode(value: CustomTypeValue<*>): LocalDateTime =
        LocalDateTime.parse((value.value as String))

    override fun encode(value: LocalDateTime): CustomTypeValue<*> =
        CustomTypeValue.GraphQLString(value.toString())
}
