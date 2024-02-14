package com.shakilpatel.notesapp.common.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.WhiteColor
import com.shakilpatel.notesapp.data.models.social.ConnectUserModel
import com.shakilpatel.notesapp.data.models.user.UserModel

@Composable
fun AddChatUserCard(user :UserModel,onClick : ()->Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 2.dp)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(WhiteColor),
        elevation = CardDefaults.cardElevation(8.dp)

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularImage(size = 50.dp, image = user.profileImg ?: ""){

            }
            Sp(w = 10.dp)
            Text(user.name.trim())

        }

    }
}
@Composable
fun ConnectedChatUserCard(user :UserModel,con : ConnectUserModel, onClick : ()->Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 2.dp)
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(WhiteColor),
        elevation = CardDefaults.cardElevation(8.dp)

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        ) {
            CircularImage(size = 50.dp, image = user.profileImg ?: ""){

            }
            Sp(w = 10.dp)
            Column(modifier = Modifier.weight(.7f)
                .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Center) {
                Text(user.name.trim())
                Text(con.lastMsg,style = MaterialTheme.typography.bodySmall)
            }
            Text(Cons.convertLongToDate(con.lastMsgTime,"hh:mm a dd MMM yy"),
                style = MaterialTheme.typography.bodySmall,
                fontSize = 9.sp,
                modifier = Modifier.padding(end = 5.dp))

        }

    }
}