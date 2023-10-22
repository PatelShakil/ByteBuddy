package com.shakilpatel.notesapp.ui.main.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shakilpatel.notesapp.R
import com.shakilpatel.notesapp.common.Cons
import com.shakilpatel.notesapp.common.HorizontalBrush
import com.shakilpatel.notesapp.common.MainColor
import com.shakilpatel.notesapp.common.WhiteColor
import com.shakilpatel.notesapp.common.uicomponents.CircularImage
import com.shakilpatel.notesapp.common.uicomponents.Sp
import com.shakilpatel.notesapp.data.models.admin.ContactsModel
import com.shakilpatel.notesapp.ui.theme.ByteBuddyTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutUsScreen(viewModel: ProfileViewModel) {
    ByteBuddyTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .padding(top = 80.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(HorizontalBrush, RoundedCornerShape(20.dp))
                        .border(1.dp, MainColor, RoundedCornerShape(18.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Sp(80.dp)
                    Text("Developed and Maintained by ")
                    Text("MuhammadShakil Patel", fontWeight = FontWeight.Bold)
                    Sp(10.dp)
                    Text(
                        viewModel.aboutMsg.value, style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.fillMaxWidth(.9f),
                        textAlign = TextAlign.Center
                    )
                    Sp(10.dp)
                    val contactsList = listOf<ContactsModel>(
                        ContactsModel(
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/WhatsApp.svg/120px-WhatsApp.svg.png",
                            "Whatsapp",
                            "https://wa.me/919510634082"
                        ),
                        ContactsModel(
                            "https://upload.wikimedia.org/wikipedia/en/thumb/c/c4/Snapchat_logo.svg/100px-Snapchat_logo.svg.png",
                            "Snapchat",
                            "https://snapchat.com/add/patelshakil95"
                        ), ContactsModel(
                            "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png",
                            "Github",
                            "https://github.com/PatelShakil"
                        ),

                        ContactsModel(
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Telegram_2019_Logo.svg/121px-Telegram_2019_Logo.svg.png",
                            "Telegram",
                            "https://t.me/patelshakil95"
                        ),
                        ContactsModel(
                            "https://pbs.twimg.com/profile_images/1661161645857710081/6WtDIesg_400x400.png",
                            "Linkedin",
                            "https://www.linkedin.com/in/muhammadshakil-mustak-75775623a"
                        ), ContactsModel(
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Instagram_logo_2022.svg/150px-Instagram_logo_2022.svg.png",
                            "Instagram",
                            "https://instagram.com/patelshakil95"
                        )
                    )
                    Sp(h = 20.dp)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(.96f),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        contactsList.forEach {
                            Box(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .padding(bottom = 5.dp)
                            ) {
                                ContactSocialCard(it)
                            }

                        }
                    }
                    val context = LocalContext.current
                    Sp(h = 10.dp)
                    Text("Made with ❤")
                    Sp(h = 5.dp)
                    Text(
                        "Version ${Cons.getCurrentVersionName(context)}",
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp
                    )
                    Sp(h = 30.dp)
                }

            }
            var devProfile by remember { mutableStateOf("") }
            viewModel.database.reference.child("devProfile")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            devProfile = snapshot.value.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            if (devProfile == "") {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_round_img),
                    contentDescription = "",
                    Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .align(Alignment.TopCenter)
                        .background(WhiteColor, CircleShape)
                        .border(1.dp, MainColor, CircleShape)
                )
            } else {
                AsyncImage(
                    model = devProfile,
                    contentDescription = "",
                    Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .align(Alignment.TopCenter)
                        .background(WhiteColor, CircleShape)
                        .border(1.dp, MainColor, CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_profile_round_img)
                )
            }

        }
    }
}

@Composable
fun ContactSocialCard(data: ContactsModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
//            .fillMaxWidth()
            .width(150.dp)
            .clickable {
                Cons.gotoUrl(data.url, context)
            },
        border = BorderStroke(.5.dp, MainColor),
        colors = CardDefaults.cardColors(
            WhiteColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Sp(w = 10.dp)
            CircularImage(image = data.icon)
            Sp(w = 10.dp)

            Text(data.platformName, style = MaterialTheme.typography.titleSmall)

        }
    }
}