package grade.xyj.com.login

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

var DEFAULT_USER_AGENT =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36 Edg/83.0.478.37"

fun OkHttpClient.get(url: String, headers: Map<String, String>? = null) =
    newCall(Request.Builder().apply {
        url(url)
        headers?.run {
            for ((k, v) in headers) addHeader(k, v)
        }
        get()
    }.build()).execute()

fun OkHttpClient.post(url: String, headers: Map<String, String>?, requestBody: RequestBody): Response {

    val request = Request.Builder().apply {
        url(url)
        headers?.run { for ((k, v) in headers) addHeader(k, v) }
        post(requestBody)
        if(headers?.get("User-Agent") == null){
            addHeader("User-Agent", DEFAULT_USER_AGENT)
        }
    }.build()

    return newCall(request).execute()
}

fun OkHttpClient.post(url: String, forms: Map<String, String>? = null, headers: Map<String, String>? = null): Response {
    val requestBody = FormBody.Builder().apply {
        forms?.run { for ((k, v) in forms) add(k, v) }
    }.build()
    return post(url, headers, requestBody)
}

fun OkHttpClient.post(url: String, json: String, headers: Map<String, String>? = null): Response? {
    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    return post(url, headers, requestBody)
}