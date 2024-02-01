package net.nomia.pos.core.provider

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ResourcesProvider {

    fun getString(@StringRes resId: Int, vararg args: Any): String
}

class ResourcesProviderImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ResourcesProvider {

    override fun getString(resId: Int, vararg args: Any): String = appContext.getString(resId, *args)
}
