package com.github.aptemkov.locationreminder

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
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
import javax.inject.Inject


@AndroidEntryPoint
class LocationService : Service() {

    @Inject lateinit var subscribeToTaskListUseCase: SubscribeToTaskListUseCase

    @Inject lateinit var firebaseTaskStorage: FirebaseTaskStorage


    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    private val tasksMutable = MutableLiveData<List<Task>>()
    val tasksLiveData: LiveData<List<Task>> get() = tasksMutable

    var tasksObserver: Observer<List<Task>>? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        tasksObserver = Observer<List<Task>> {
            locationClient.updateTasksList(it)
        }

        serviceScope.launch {
            firebaseTaskStorage.startTasksListenerFromService {
                Log.i("TEST", "service got list: $it")
                tasksMutable.value = it
            }
        }

        tasksLiveData.observeForever(tasksObserver!!)

        Log.i("SERVICE", "Service started, ${tasksLiveData.value?.first()}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(false)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(5000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->

                tasksLiveData.value?.let { list ->
                    for(task in list) {

                        val taskLocation = Location("")
                        taskLocation.latitude = task.latitude
                        taskLocation.longitude = task.longitude
                        val distance = location.distanceTo(taskLocation)

                            if(distance <= task.reminderRange) {
                                //val lat = location.latitude.toString()
                                //val long = location.longitude.toString()
                                val updatedNotification = notification.setContentText(
                                    "${tasksLiveData.value?.size} Location ${task.title} is $distance meters away."
                                )
                                notificationManager.notify(1, updatedNotification.build())
                            }
                            else notificationManager.cancelAll()
                    }
                }


            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    override fun onUnbind(intent: Intent?): Boolean {
        tasksLiveData.removeObserver(tasksObserver!!)
        return super.onUnbind(intent)
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