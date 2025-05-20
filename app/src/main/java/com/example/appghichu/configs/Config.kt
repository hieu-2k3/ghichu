package com.example.appghichu.configs

object Config {
    val url_terms = "https://avatar-maker.sboomtools.net/terms-of-use.html"
    val url_privacy = "https://avatar-maker.sboomtools.net/privacy.html"
    fun apiChat(): String {
//        if(BuildConfig.DEBUG){
//            return "http://35.239.132.21:8002/"
//        }
        return "https://chatopenai.sboomtools.net"
    }
}