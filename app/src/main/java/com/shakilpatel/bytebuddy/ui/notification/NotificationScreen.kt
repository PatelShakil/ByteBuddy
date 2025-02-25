package com.shakilpatel.bytebuddy.ui.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shakilpatel.bytebuddy.R
import com.shakilpatel.bytebuddy.common.Cons
import com.shakilpatel.bytebuddy.common.NotificationUnReadColor
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.WhiteColor
import com.shakilpatel.bytebuddy.common.uicomponents.CircularImage
import com.shakilpatel.bytebuddy.common.uicomponents.OnNoDataFound
import com.shakilpatel.bytebuddy.common.uicomponents.ProgressBarIndicator
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.data.models.user.NotificationModel
import com.shakilpatel.bytebuddy.ui.main.home.notes.NotesViewModel
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme

@Composable
fun NotificationScreen(viewModel: NotificationViewModel,notesViewModel: NotesViewModel,navController: NavController) {
    ByteBuddyTheme {

        viewModel.notifications.collectAsState().value.let {
            when(it){
                is Resource.Loading ->{
                    ProgressBarIndicator()
                }
                is Resource.Success ->{
                    ListNotifications(list = it.result.sortedBy { it.date }.reversed(), viewModel = viewModel, notesViewModel, navController)
                }
                is Resource.Failure ->{
                    OnNoDataFound(msg = it.errorMsgBody)
                }
            }
        }
    }
}

@Composable
fun ListNotifications(list : List<NotificationModel>,viewModel: NotificationViewModel,notesViewModel: NotesViewModel,navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxWidth()){
        item{
            Sp(h = 10.dp)
        }
        items(list){
            NotificationItem(data = it, viewModel = viewModel,notesViewModel,navController)
        }
        item{
            Sp(h = 100.dp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NotificationItem(data : NotificationModel, viewModel: NotificationViewModel, notesViewModel : NotesViewModel, navController: NavController) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 2.dp)
        .clickable {
            if (!data.read)
                viewModel.markRead(id = data.id)

            if (data.faq != "") {
                navController.navigate(Screen.Main.Feed.Landing.route + "/${data.faq}")

            } else if(data.notesId != "") {
                notesViewModel.notesId.value = data.notesId
                navController.navigate(Screen.ViewNotes.Landing.route + "/${data.notesId}")
                notesViewModel.registerView(data.notesId)
            }
        },
        colors = CardDefaults.cardColors(
            if(data.read)
                WhiteColor
            else
                NotificationUnReadColor
        ),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(18.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically){
            Sp(w=5.dp)
            CircularImage(icon = if(data.faq != "") R.drawable.ic_feed else if(data.notesId != "") R.drawable.ic_notes else R.drawable.ic_notifications,35.dp)
            Sp( w = 5.dp)
            Column(modifier = Modifier){
                FlowRow(modifier = Modifier.fillMaxWidth()){
                    Text(data.title,
                        modifier = Modifier.weight(.8f),
                        style = MaterialTheme.typography.titleSmall)
                    Text(Cons.convertLongToDate(data.date, "hh:mma dd/MMM"), fontSize = 9.sp,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .weight(.3f),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.titleSmall)


                }
                Sp(h = 3.dp)
                Text(data.message,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp)


            }

        }
    }
}