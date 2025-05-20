package com.example.appghichu.manager.ads

import android.app.Activity

class AdsManager {
    var admob = AdmobAds()

    fun preload(activity: Activity){
        admob.preLoad(activity)
    }

    //Reward
    fun showReward(activity: Activity?, callback: (RewardStatus) -> Unit){
        activity?.let { requireActivity ->
            admob.showReward(requireActivity, callback)
        }
    }
}