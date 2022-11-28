package red.tetracube.tetracubeapp.core.extensions

import java.net.URI

fun String.apiAddress(): String {
    return try {
        val apiBaseURL = URI(
            "http",
            null,
            this,
            8080,
            null,
            null,
            null
        )
        apiBaseURL.toString()
    } catch (ex: Exception) {
        "http://localhost:8080"
    }
}