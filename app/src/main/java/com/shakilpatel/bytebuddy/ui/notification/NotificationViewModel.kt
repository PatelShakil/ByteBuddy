package com.shakilpatel.bytebuddy.ui.notification

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.data.models.user.NotificationModel
import com.shakilpatel.bytebuddy.data.repo.NotiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val auth : FirebaseAuth,
    private val notiRepo: NotiRepo
): ViewModel() {

    val notifications = MutableStateFlow<Resource<List<NotificationModel>>>(Resource.Loading)

    init {
        getAllNotifications(auth.uid.toString())
    }
    fun getAllNotifications(uid : String){
        notiRepo.getAllNotifications(uid){
            notifications.value = it
        }
    }

    fun markRead(uid: String = auth.uid.toString(),id:String){
        notiRepo.markReadNotification(id,uid)
    }







}