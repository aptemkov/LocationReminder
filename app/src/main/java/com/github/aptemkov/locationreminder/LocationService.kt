package com.github.aptemkov.locationreminder

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.github.aptemkov.locationreminder.data.storage.FirebaseTaskStorage
import com.github.aptemkov.locationreminder.domain.models.Task
import com.github.aptemkov.locationreminder.domain.usecases.SubscribeToTaskListUseCase
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.security.Permission
import javax.inject.Inject


@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var subscribeToTaskListUseCase: SubscribeToTaskListUseCase

    @Inject
    lateinit var firebaseTaskStorage: FirebaseTaskStorage

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    private val tasksMutable = MutableLiveData<List<Task>>()
    val tasksLiveData: LiveData<List<Task>> get() = tasksMutable

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        serviceScope.launch {
            firebaseTaskStorage.startTasksListenerFromService {
                tasksMutable.value = it
            }
        }

        Log.i("SERVICE", "Service started, ${tasksLiveData.value?.first()}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun start() {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location")
            .setContentText(getString(R.string.location_tracking_enabled))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_launcher_background, "Stop tracking", pendingIntent)
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(5000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->

                tasksLiveData.value?.let { list ->
                    for (task in list) {
                        val taskLocation = Location("").apply {
                            latitude = task.latitude
                            longitude = task.longitude
                        }
                        val distance = location.distanceTo(taskLocation)

                        if (task.active && distance <= task.reminderRange) {
                            val updatedNotification =
                                notification

                                    .setContentText("Location ${task.title} is $distance meters away."
                            )
                            notificationManager.notify(1, updatedNotification.build())

                            vibrate()
                            makeSound()
                            break
                        } else {
                            val updatedNotification = notification.setContentText(
                                getString(R.string.location_tracking_enabled)
                            )
                            notificationManager.notify(1, updatedNotification.build())
                        }
                    }
                }
            }.launchIn(serviceScope)


        startForeground(1, notification.build())
    }


    private fun makeSound() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.notification_sound)
        mediaPlayer.start()
        Log.i("Sound", "successful")
    }

    @SuppressLint("MissingPermission")
    private fun vibrate() {
/*
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        Log.i("VIBRATOR", "has vibrator = ${vibrator.hasVibrator()}")
        if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
                Log.i("VIBRATOR", "vibrated on sdk >= 26")
            } else {
                Log.i("VIBRATOR", "vibrated on sdk < 26")
                vibrator.vibrate(500) // Vibrate method for below API Level 26
            }
        }
        else Log.i("VIBRATOR", "failed")
*/

        val vibrator = ContextCompat.getSystemService(this, Vibrator::class.java) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
            Log.d("VIBRATOR", "Vibration started")
        } else {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(200)
                Log.d("VIBRATOR", "Vibration started")
            } else {
                Log.d("VIBRATOR", "Vibration not supported")
            }
        }

    }


    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}