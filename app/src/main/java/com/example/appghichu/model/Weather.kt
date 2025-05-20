package com.example.appghichu.model

data class Weather(var weather : ArrayList<Statuswt>, var main: Main, var wind: Wind,var clouds: Cloud, var dt : Long,var timezone : Long, var name : String) {
}

data class Statuswt(var id : Int, var main : String, var description : String, var icon : String) {
}

data class Main(var temp : Double, var humidity : Int) {
}

data class Wind(var speed : Float, var deg : Int, var gust : Float) {
}

data class Cloud(var all : Int) {
}