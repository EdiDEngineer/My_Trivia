package edu.utap.mytrivia.data.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.utap.mytrivia.data.remote.model.TriviaResponse
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApi {

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: String,
        @Query("difficulty") difficulty: String,
        @Query("type") type: String,
        @Query("category") category: String
    ): Response<TriviaResponse>

    companion object {

        private val url = HttpUrl.Builder()
            .scheme("https")
            .host("opentdb.com")
            .build()

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        fun create(client: OkHttpClient): TriviaApi = create(url, client)

        private fun create(httpUrl: HttpUrl, client: OkHttpClient): TriviaApi {
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(TriviaApi::class.java)
        }
    }
}