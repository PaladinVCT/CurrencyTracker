package by.lebedev.core.network.interceptor

import by.lebedev.core.util.decodeFromBase64
import okhttp3.Interceptor
import okhttp3.Response


internal class HttpRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val modifiedRequest = request
            .newBuilder()
            .header(
                "apikey",
                decodeFromBase64("QXNCQkZGdzB5eUNaRDFFRUhNRkZZOWVieTFSQUk0N0c=").orEmpty()
            )
            .build()

        val response = chain.proceed(modifiedRequest)

        return response
    }
}