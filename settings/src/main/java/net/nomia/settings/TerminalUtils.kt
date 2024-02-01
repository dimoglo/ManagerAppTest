package net.nomia.settings

import android.content.Context
import net.nomia.common.data.model.Terminal
import net.nomia.common.data.model.Terminal.DeviceType.Phone
import net.nomia.common.data.model.Terminal.DeviceType.Tablet

fun Context.getDeviceType(): Terminal.DeviceType {
    val isTablet = resources.getBoolean(R.bool.isTablet)

    return if (isTablet) Tablet else Phone
}

