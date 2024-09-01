package com.shakilpatel.notesapp.ui.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

@Composable
fun AdmobBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier= modifier,
        factory = {context->
            AdView(context).apply {
                // on below line specifying ad size
                //adSize = AdSize.BANNER
                // on below line specifying ad unit id
                // currently added a test ad unit id.
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-5228160324628992/9166001708"
                // calling load ad to load our ad.
                loadAd(AdRequest.Builder().build())
            }
    })
}