package com.example.appghichu.model

import java.io.Serializable

class DataPopulars(
    var items:ArrayList<Prompt> = arrayListOf(),
    var pagination:Pagination = Pagination(total = 0,
        per_page = 0,
        current_page = 0,
        total_pages = 0)
): Serializable

//@Parcelize
class Pagination(
    var total: Int = 0,
    var per_page: Int = 0,
    var current_page: Int = 0,
    var total_pages: Int = 0
) : Serializable