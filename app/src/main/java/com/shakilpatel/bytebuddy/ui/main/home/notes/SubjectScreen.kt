package com.shakilpatel.bytebuddy.ui.main.home.notes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shakilpatel.bytebuddy.common.HorizontalBrush
import com.shakilpatel.bytebuddy.common.Resource
import com.shakilpatel.bytebuddy.common.TextColor
import com.shakilpatel.bytebuddy.common.getHorizontalGradient
import com.shakilpatel.bytebuddy.common.uicomponents.Sp
import com.shakilpatel.bytebuddy.common.uicomponents.SubjectAppBar
import com.shakilpatel.bytebuddy.ui.nav.Screen
import com.shakilpatel.bytebuddy.ui.theme.ByteBuddyTheme

@Composable
fun SubjectScreen(course: String, viewModel: NotesViewModel, navController: NavController) {
    val sem = course.replace("BCA", "").replace("MSC ICT","")
    viewModel.courses.collectAsState().value.let {
        when (it) {
            is Resource.Success -> {
                ByteBuddyTheme {

                    Column(modifier = Modifier
                        .fillMaxSize()
                        .background(HorizontalBrush)) {
                        SubjectAppBar(sem, navController)
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 20.dp)
                        ) {

                            if (sem.isNotEmpty()) {
                                Log.d("Course", "SubjectScreen: $course")
                                it.result.find { course.contains(it.name) }?.semList?.filter { it.name == sem }
                                    ?.get(0)?.subjects?.sortedBy {
                                    it.id.substring(
                                        0,
                                        3
                                    )
                                }?.forEachIndexed { index, subjectModel ->
                                    item {
                                        SubjectItem(
                                            index = subjectModel.id,
                                            name = subjectModel.subjectName
                                        ) {
                                            viewModel.setSubject(subjectModel.id)
                                            viewModel.getNotesCol(subjectModel.subjectName, course)
                                            navController.navigate(Screen.Main.Home.Notes.route + "/${subjectModel.subjectName}/${course}")

                                        }
                                        Sp(10.dp)
                                    }
                                }
                            }

                        }
                    }
                }
            }

            else -> {}
        }
    }

}

@Composable
fun SubjectItem(index: String, name: String, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(.95f)
            .clickable { onClick(name) },
        elevation = CardDefaults.cardElevation(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(getHorizontalGradient()),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "$index. ",
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(start = 15.dp)
                    .weight(.25f),
                color = TextColor
            )
            Text(
                text = "$name",
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .weight(.8f),
                color = TextColor
            )
        }
    }
}