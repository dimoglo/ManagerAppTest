package net.nomia.pos.core.utils

import android.content.Context
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import timber.log.Timber

private const val VIBRATION_DURATION = 500L

@RequiresPermission(android.Manifest.permission.VIBRATE)
fun Context.playNotifySound() {
    val audioManager = ContextCompat.getSystemService(this, AudioManager::class.java)

    when (audioManager?.ringerMode) {
        AudioManager.RINGER_MODE_VIBRATE -> {
            ContextCompat.getSystemService(this, Vibrator::class.java)?.also { vibrator ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            VIBRATION_DURATION,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(VIBRATION_DURATION)
                }
            }
        }
        AudioManager.RINGER_MODE_NORMAL -> {
            try {
                val notificationRingtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val notificationRingtone = RingtoneManager.getRingtone(
                    this,
                    notificationRingtoneUri
                )
                notificationRingtone.play()
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                Timber.e(e)
            }
        }
    }
}
