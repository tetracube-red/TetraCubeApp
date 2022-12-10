package red.tetracube.tetracubeapp.data.repositories.remote.housedevicesmesh

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.core.extensions.apiAddress
import red.tetracube.tetracubeapp.data.repositories.remote.housedevicesmesh.payloads.GetHouseMeshDescriptionResponse
import java.net.ConnectException

class HouseDevicesMeshAPIClient(
    private val apiUrl: String,
    private val bearerToken: String
) {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 1000
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(bearerToken, "")
                }
            }
        }
        Charsets {
            register(Charsets.UTF_8)
        }
    }

    val serviceCallStatus =  MutableSharedFlow<ServiceCallStatus>()

    suspend fun getHouseMeshDescription() : GetHouseMeshDescriptionResponse? {
        serviceCallStatus.emit(ServiceCallStatus.IDLE)
        serviceCallStatus.emit(ServiceCallStatus.CONNECTING)

        val requestUrl = "${apiUrl.apiAddress()}/house/mesh"
        try {
            val response: HttpResponse = client.get(requestUrl) {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
            delay(200)

            if (response.contentType() != null && !response.contentType()!!.match(ContentType.Application.Json)) {
                serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_NOT_FOUND)
                return null
            }
            when (response.status) {
                HttpStatusCode.NotFound -> {
                    serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_NOT_FOUND)
                    return null
                }
                HttpStatusCode.Forbidden, HttpStatusCode.Unauthorized -> {
                    serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_UNAUTHORIZED)
                    return null
                }
            }

            serviceCallStatus.emit(ServiceCallStatus.FINISHED_SUCCESS)
            return response.body()
        } catch (ex: Exception) {
            if (ex is ConnectException) {
                serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_CONNECTION)
                return null
            } else if (ex is HttpRequestTimeoutException) {
                serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_TIMEOUT)
                return null
            }
            Log.e("getHouseMeshDescription", "${ex.message}")
            serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_UNKNOWN)
            return null
        }
    }
}