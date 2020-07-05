package com.zoewave.myapplication.api

import com.squareup.moshi.Json


class ExampleAPI {

    @Json(name = "userId")
    var userId: Int? = null

    @Json(name = "id")
    var id: Int? = null

    @Json(name = "title")
    var title: String? = null

    @Json(name = "completed")
    var completed: Boolean? = null

}


// use to generate API Classes
// http://www.jsonschema2pojo.org/

//https://jsonplaceholder.typicode.com/todos/1

