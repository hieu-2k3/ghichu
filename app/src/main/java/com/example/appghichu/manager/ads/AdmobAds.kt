package com.example.appghichu.manager.ads

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import com.example.appghichu.MainActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

enum class RewardStatus{
    EARNED,DISMISS,FAILED
}
object AdmobObject {
    var adsReward : RewardedAd? = null
}

class AdmobAds {
    var isRewardLoading : Boolean = false
    private val AD_UNIT_ID = ""
    companion object {
        const val TAG = "ADMOB"

        fun config(context: Context, appId: String){

            try {
                val ai: ApplicationInfo = context.packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA
                )
                val bundle = ai.metaData
                val myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID")
                Log.e("AppApplication", "Name Found: $myApiKey")
                ai.metaData.putString(
                    "com.google.android.gms.ads.APPLICATION_ID",
                    appId
                )

            } catch (e: PackageManager.NameNotFoundException) {
                Log.e("AppApplication", "Failed to load meta-data, NameNotFound: " + e.message)
            } catch (e: NullPointerException) {
                Log.e("AppApplication", "Failed to load meta-data, NullPointer: " + e.message)
            }
        }
    }

    fun preLoad(activity: Activity){
        loadReward(activity)
    }


    //ADS REWARD
    fun loadReward(activity: Activity){

        Log.e("loadReward", "loadReward")

        if (AdmobObject.adsReward == null && !isRewardLoading) {

            val adRequest = AdRequest.Builder().build()
            isRewardLoading = true
            RewardedAd.load(activity, AD_UNIT_ID,
                adRequest, object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        // Handle the error.
                        Log.e(TAG, loadAdError.message)
                        isRewardLoading = false
                        AdmobObject.adsReward = null
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        isRewardLoading = false
                        AdmobObject.adsReward = rewardedAd
                        Log.e(TAG, "reward Ad was loaded.")
                    }
                })
        }
    }
    fun showReward(activity: Activity, callback: (RewardStatus) -> Unit){

        AdmobObject.adsReward?.let {
            it.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.e("Ad was dismissed.", "Ad was dismissed.")
                    AdmobObject.adsReward = null
                    callback(RewardStatus.DISMISS)
                    loadReward(activity)
                }
                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    Log.e("Ad failed to show.", "Ad failed to show.")
                    AdmobObject.adsReward = null
                    callback(RewardStatus.FAILED)
                    loadReward(activity)
                }
                override fun onAdShowedFullScreenContent() {
                    Log.e("Ad was shown.", "Ad was shown.")
                }
            }
            it.setOnPaidEventListener { adValue ->
//                AppAnalytics.sendOnPaidEvent(adValue,
//                    it.adUnitId,
//                    detectAdsAtScreen(activity, "REWARD ADS"),
//                    it.responseInfo.mediationAdapterClassName ?: "",
//                    activity)
            }
            it.show(activity){
                callback(RewardStatus.EARNED)
                Log.e("User earned the reward.", "User earned the reward.")
            }
        } ?: run {
            Log.e("ads null && loading =", "ads null && loading = $isRewardLoading")
            callback(RewardStatus.FAILED)
            loadReward(activity)
        }
    }

    //ADS BANNER


//    private fun detectAdsAtScreen(activity: Activity, adsType:String) : String{
//        var placement = ""
//
//        if (activity is MainActivity){
//
//        }else{
//            if (activity.javaClass.simpleName.contains("Activity", ignoreCase = true)){
//                placement = activity.javaClass.simpleName.replace("Activity","_Screen")
//                Log.e("Ads At Screens","Ads Type : $adsType - Placement : $placement")
//            }
//        }
//
//        (activity as BaseActivity).supportFragmentManager.fragments.forEach { fragmentItem ->
//
//            Logger.e("Fragment Items : ${fragmentItem.javaClass.simpleName}")
//
//            if (fragmentItem.javaClass.simpleName.contains("Fragment", ignoreCase = true)){
//                placement = fragmentItem.javaClass.simpleName.replace("Fragment","_Screen")
//                Log.e("Ads At Screens","Ads Type : $adsType - Placement : $placement")
//            }
//        }
//
//        Logger.e("=============================== Placement : $placement ===============================")
//
//        return placement
//    }
}