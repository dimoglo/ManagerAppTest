package net.nomia.pos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import net.nomia.pos.core.handler.OnApplicationCreated
import net.nomia.pos.utils.log.ActivityLifecycleBundleLogger
import javax.inject.Inject


@HiltAndroidApp
class PosApplication : Application() {

    @Inject
    lateinit var handlers: Set<@JvmSuppressWildcards OnApplicationCreated>

    override fun onCreate() {
        super.onCreate()
        handlers.forEach { it.handler(this) }
        registerActivityLifecycleCallbacks(ActivityLifecycleBundleLogger())
    }
}
