package net.nomia.pos.utils.log

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.gu.toolargetool.TooLargeTool
import timber.log.Timber

@Suppress("EmptyFunctionBlock")
class ActivityLifecycleBundleLogger : ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Timber
            .tag("onActivitySaveInstanceState")
            .w(activity.javaClass.name + ": " + TooLargeTool.bundleBreakdown(outState))
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}
