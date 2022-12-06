package red.tetracube.tetracubeapp.data.repositories.remote.account

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.flow.MutableSharedFlow
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.core.extensions.apiAddress
import red.tetracube.tetracubeapp.data.repositories.remote.account.payloads.UserLoginRequest
import red.tetracube.tetracubeapp.data.repositories.remote.account.payloads.UserLoginResponse
import java.net.ConnectException

class UserAPIClient {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 1000
        }
        Charsets {
            register(Charsets.UTF_8)
        }
    }

    val serviceCallStatus =  MutableSharedFlow<ServiceCallStatus>()

    suspend fun userLogin(
        username: String,
        authenticationCode: String,
        apiServiceAddress: String
    ): UserLoginResponse? {
        serviceCallStatus.emit(ServiceCallStatus.IDLE)
        serviceCallStatus.emit(ServiceCallStatus.CONNECTING)

        val enrollmentRequest = UserLoginRequest(username, authenticationCode)
        val requestUrl = "${apiServiceAddress.apiAddress()}/users/login"
        try {
            val response: HttpResponse = client.post(requestUrl) {
                contentType(ContentType.Application.Json)
                setBody(enrollmentRequest)
                accept(ContentType.Application.Json)
            }

            if (response.contentType() != null && !response.contentType()!!.match(ContentType.Application.Json)) {
                serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_NOT_FOUND)
                return null
            }
            when (response.status) {
                HttpStatusCode.NotFound -> {
                    serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_NOT_FOUND)
                    return null
                }
                HttpStatusCode.BadRequest -> {
                    serviceCallStatus.emit(ServiceCallStatus.FINISHED_INVALID_TOKEN)
                    return null
                }
                HttpStatusCode.Conflict -> {
                    serviceCallStatus.emit(ServiceCallStatus.FINISHED_CONFLICTING)
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
            Log.e("accountEnrollment", "${ex.message}")
            serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_UNKNOWN)
            return null
        }
    }
}