package com.example.impostergame

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerAdView() {
    AndroidView(factory = { context ->
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = "ca-app-pub-2086245291423900/5482891693" // ✅ Test banner
            loadAd(AdRequest.Builder().build())
        }
    }, update = {
        it.loadAd(AdRequest.Builder().build())
    })
}