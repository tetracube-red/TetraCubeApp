package red.tetracube.tetracubeapp.core.extensions

import java.net.URI

fun String.apiAddress(): String {
    val apiBaseURL = URI(
        "http",
        null,
        this,
        8080,
        null,
        null,
        null
    )
    return apiBaseURL.toString()
}