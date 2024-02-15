package com.shakilpatel.notesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.uicomponents.UpdateDialogue
import com.shakilpatel.notesapp.data.models.user.UserModel
import com.shakilpatel.notesapp.data.notification.Cons.TOPIC_ALL
import com.shakilpatel.notesapp.ui.auth.AuthViewModel
import com.shakilpatel.notesapp.ui.nav.NotesAppNavHost
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ByteBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val viewModel = hiltViewModel<AuthViewModel>()
                    LaunchedEffect(true){
                        viewModel.registerUserToken()
                    }
                    Cons.requestPermissionsIfNecessary(this)
                    FirebaseMessaging.getInstance().subscribeToTopic("all")
                    var isLatestVersion: Boolean? by remember { mutableStateOf(null) }
                    var latestVersion by remember { mutableStateOf("") }
                    val database = FirebaseDatabase.getInstance()
                    val context = LocalContext.current
                    database.reference.child("version")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    isLatestVersion =
                                        Cons.getCurrentVersionCode(context) == snapshot.value.toString()
                                            .toInt()
                                    latestVersion = snapshot.value.toString()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                    if (isLatestVersion == null || isLatestVersion == true) {
                        NotesAppNavHost() {
                            finishAffinity()
                        }
                    }
                    if (isLatestVersion == false) {
                        UpdateDialogue(latestVersion) {
                            finishAffinity()
                        }
                    }
                }
            }
        }



//        startActivity(Intent(this,AuthActivity::class.java))

//        val viewModel = SplashViewModel(AuthRepo())
//        if(viewModel.checkUserExist()){
//            viewModel.isUserExists(this)
//        }else
//            startActivity(Intent(this,LoginActivity::class.java))
    }

    fun setUserOnline(){
        val userRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().uid.toString())
        userRef.get().addOnSuccessListener {
            if(it.exists()){
                val user = it.toObject(UserModel::class.java)!!
                user.online = true
                user.lastSeen = System.currentTimeMillis()
                userRef.set(user)
            }
        }

    }

    fun setUserOffline(){
        val userRef = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().uid.toString())
        userRef.get().addOnSuccessListener {
            if(it.exists()){
                val user = it.toObject(UserModel::class.java)!!
                user.online = false
                user.lastSeen = System.currentTimeMillis()
                userRef.set(user)
            }
        }

    }
    fun log(msg:String){
        Log.d("Lifecycle",msg)
    }

    override fun onStart() {
        super.onStart()
        setUserOnline()
        log("onStart")
    }
    override fun onStop() {
        super.onStop()
        setUserOffline()
        log("onStop")
    }
    override fun onDestroy() {
        super.onDestroy()
        setUserOffline()
        log("onDestroy")
    }
    override fun onRestart() {
        super.onRestart()
        setUserOnline()
        log("onRestart")
    }
    override fun onResume() {
        super.onResume()
        setUserOnline()
        log("onResume")
    }
    override fun onPause() {
        super.onPause()
        setUserOffline()
        log("onPause")
    }
    override fun onBackPressed() {
        super.onBackPressed()
        setUserOffline()
        log("onBackPressed")
    }

}