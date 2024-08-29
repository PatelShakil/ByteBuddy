package com.shakilpatel.notesapp.ui.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

fun showInterstialAd(context : Activity,onDone : () -> Unit) {
    InterstitialAd.load(
        context,
        "ca-app-pub-6374179772242710/5275987450", //Change this with your own AdUnitID!
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                onDone()
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                interstitialAd.show(context)
                onDone()
            }
        }
    )
}