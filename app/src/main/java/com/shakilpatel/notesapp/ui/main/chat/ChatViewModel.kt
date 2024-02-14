package com.shakilpatel.notesapp.ui.main.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.Resource
import com.shakilpatel.notesapp.data.models.social.ChatModel
import com.shakilpatel.notesapp.data.models.social.ConnectUserModel
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader.init
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    //chat related operations without repos

    val uid = auth.uid.toString()
    private val _connectedUsers = MutableStateFlow<Resource<Pair<List<UserModel>,List<ConnectUserModel>>>?>(null)
    val connectedUsers = _connectedUsers

    private val _allUsers = MutableStateFlow<Resource<List<UserModel>>?>(null)
    val allUsers = _allUsers

    private val _recieverUser = MutableStateFlow<Resource<UserModel>?>(null)
    val recieverUser = _recieverUser

    private val _chats = MutableStateFlow<Resource<List<ChatModel>>?>(null)
    val chats = _chats


    init {
        getAllUsers()
        getConnectedUsers()
    }

    fun getChats(recieverId: String) = viewModelScope.launch {
        _chats.value = Resource.Loading
        db.collection("conversations")
            .where(
                Filter.or(
                Filter.and(
                    Filter.equalTo("senderId", uid),
                    Filter.equalTo("receiverId", recieverId),
                ),
                    Filter.and(
                        Filter.equalTo("senderId", recieverId),
                        Filter.equalTo("receiverId", uid),
                    )
                )
            )
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _chats.value =
                        Resource.Failure(errorMsgBody = error.message.toString(), ex = error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val chats = mutableListOf<ChatModel>()
                    for (doc in value.documents) {
                        val chat = doc.toObject(ChatModel::class.java)
                        if (chat != null) {
                            chats.add(chat)
                        }
                    }
                    _chats.value = Resource.Success(chats)
                }
            }
    }


    fun getRecieverUser(uid: String) = viewModelScope.launch {
        _recieverUser.value = Resource.Loading
        db.collection("users")
            .document(uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _recieverUser.value =
                        Resource.Failure(errorMsgBody = error.message.toString(), ex = error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val user = value.toObject(UserModel::class.java)
                    if (user != null) {
                        _recieverUser.value = Resource.Success(user)
                    }
                }
            }
    }

    fun getAllUsers() = viewModelScope.launch {
        _allUsers.value = Resource.Loading
        db.collection("users")
            .whereNotEqualTo("uid", uid.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _allUsers.value =
                        Resource.Failure(errorMsgBody = error.message.toString(), ex = error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val users = mutableListOf<UserModel>()
                    for (doc in value.documents) {
                        val user = doc.toObject(UserModel::class.java)
                        if (user != null) {
                            users.add(user)
                        }
                    }
                    _allUsers.value = Resource.Success(users)
                }
            }
    }

    fun getConnectedUsers() = viewModelScope.launch {
        _connectedUsers.value = Resource.Loading
        try {
            db.collection("chats")
                .whereEqualTo("senderId", uid)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        _connectedUsers.value =
                            Resource.Failure(errorMsgBody = error.message.toString(), ex = error)
                        return@addSnapshotListener
                    }

                    if (value != null) {
                        val users = mutableListOf<UserModel>()
                        val chats = mutableListOf<ConnectUserModel>()
                        viewModelScope.launch {
                            for (doc in value.documents) {
                                val chat = doc.toObject(ConnectUserModel::class.java)
                                if (chat != null) {
                                    val user = getUser(chat.receiverId)
                                    chats.add(chat)
                                    users.add(user)
                                }
                            }
                            _connectedUsers.value = Resource.Success(Pair(users,chats))
                        }
                    }
                }
        } catch (e: Exception) {
            _connectedUsers.value = Resource.Failure(errorMsgBody = e.message.toString(), ex = e)
        }
    }

    private suspend fun getUser(userId: String): UserModel {
        return suspendCoroutine { continuation ->
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val user = documentSnapshot.toObject(UserModel::class.java)
                    if (user != null) {
                        continuation.resume(user)
                    } else {
                        continuation.resumeWithException(NullPointerException("User not found"))
                    }
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }


        fun resetRecieverUser() {
            _recieverUser.value = null
        }

        fun sendMsg(msg: String, recieverId: String) = viewModelScope.launch {
            val id = Cons.generateRandomValue(16)
            db.collection("conversations")
                .document(id)
                .set(
                    ChatModel(
                        id,
                        senderId = FirebaseAuth.getInstance().uid.toString(),
                        receiverId = recieverId,
                        msg,
                        System.currentTimeMillis(),
                        "",
                        "",
                        ""
                    )
                )
                .addOnSuccessListener {
                    db.collection("chats")
                        .document(uid + recieverId)
                        .set(
                            ConnectUserModel(
                                uid,
                                recieverId,
                                msg,
                                System.currentTimeMillis()
                            )
                        )
                }
        }

        fun resetChats() {
            _chats.value = null
        }


}