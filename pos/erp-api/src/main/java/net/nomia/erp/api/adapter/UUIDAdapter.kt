package net.nomia.erp.api.adapter

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import java.util.UUID

class UUIDAdapter : CustomTypeAdapter<UUID> {
    override fun decode(value: CustomTypeValue<*>): UUID = UUID.fromString(value.value as String?)

    override fun encode(value: UUID): CustomTypeValue<*> =
        CustomTypeValue.GraphQLString(value.toString())
}
