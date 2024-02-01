package net.nomia.erp.api.adapter

import com.apollographql.apollo.api.CustomTypeAdapter
import com.apollographql.apollo.api.CustomTypeValue
import java.math.BigDecimal

class BigDecimalAdapter : CustomTypeAdapter<BigDecimal> {
    override fun decode(value: CustomTypeValue<*>): BigDecimal = (value.value as BigDecimal)
    override fun encode(value: BigDecimal): CustomTypeValue<*> =
        CustomTypeValue.GraphQLString(value.toString())
}
