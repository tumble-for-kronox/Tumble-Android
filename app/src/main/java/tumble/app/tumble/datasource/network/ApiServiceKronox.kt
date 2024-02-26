package tumble.app.tumble.datasource.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import tumble.app.tumble.domain.models.network.NewsItems

interface ApiServiceKronox {

    @Headers(
        "Content-Type: application/json; charset=utf-8",
        "Accept: application/json; charset=utf-8"
    )
    @GET("/api/misc/news")
    fun getNews(): Call<NewsItems>
}
