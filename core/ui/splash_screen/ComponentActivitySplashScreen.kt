package net.nomia.core.ui.splash_screen

//noinspection SuspiciousImport
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity

/**
 * SplashScreen control class
 * * [ISplashScreenController] has two methods for keep and hiding the splash screen
 * * [ISplashScreenController.keep] method must be called in the onCreate method immediately after the onCreate super
 * method. Calling this method again will not do anything.
 * * [ISplashScreenController.hide] method should be called when it is necessary to hide the splash screen. Calling this
 * method again will not do anything.
 * * [ISplashScreenController.setTheme] method for changing SplashScreen theme. The new theme takes effect after
 * restarting the application
 * */
@SuppressLint("CustomSplashScreen")
abstract class ComponentActivitySplashScreen : ComponentActivity() {

    interface ISplashScreenController {
        fun keep(activity: Activity)
        fun hide(activity: Activity)
        fun setTheme(activity: Activity, isDark: Boolean)
    }

    private var isHidedSplashScreen = false

    val splashScreenController: ISplashScreenController
        get() {
            return object : ISplashScreenController {
                override fun keep(activity: Activity) {
                    if (isHidedSplashScreen) return
                    activity.findViewById<View?>(R.id.content).apply {
                        if (tag is Boolean) return
                        tag = true
                        viewTreeObserver.addOnPreDrawListener(
                            object : ViewTreeObserver.OnPreDrawListener {
                                override fun onPreDraw(): Boolean {
                                    val isKeep = tag as Boolean
                                    if (!isKeep) viewTreeObserver.removeOnPreDrawListener(this)
                                    return !isKeep
                                }
                            }
                        )
                    }
                }

                override fun hide(activity: Activity) {
                    activity.findViewById<View?>(R.id.content).apply { tag = false }
                }

                override fun setTheme(activity: Activity, isDark: Boolean) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val themeName = if (isDark) "Theme.Nomia.Splash.Dark" else "Theme.Nomia.Splash.Light"
                        val themeId = resources.getIdentifier(themeName, "style", packageName)
                        activity.splashScreen.setSplashScreenTheme(themeId)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            isHidedSplashScreen = it.getBoolean("IsHidedSplashScreen")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("IsHidedSplashScreen", true)
    }
}
