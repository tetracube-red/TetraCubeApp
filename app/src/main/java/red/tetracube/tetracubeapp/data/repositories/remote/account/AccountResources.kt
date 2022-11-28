package red.tetracube.tetracubeapp.data.repositories.remote.account

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.core.extensions.apiAddress
import red.tetracube.tetracubeapp.data.repositories.remote.account.payloads.RegistrationRequest
import java.net.ConnectException

class AccountResources {

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

    /*  var serviceCallStatus by mutableStateOf(ServiceCallStatus.IDLE)
          private set*/
    val serviceCallStatus =  MutableSharedFlow<ServiceCallStatus>()

    suspend fun accountRegistration(
        username: String,
        password: String,
        apiServiceAddress: String,
        invitationCode: String
    ) {
        serviceCallStatus.emit(ServiceCallStatus.IDLE)
        serviceCallStatus.emit(ServiceCallStatus.CONNECTING)

        val enrollmentRequest = RegistrationRequest(username, password, invitationCode)
        val requestUrl = "${apiServiceAddress.apiAddress()}/account/registration"
        try {
            val response: HttpResponse = client.post(requestUrl) {
                contentType(ContentType.Application.Json)
                setBody(enrollmentRequest)
                accept(ContentType.Application.Json)
            }

            if (response.contentType() != null && !response.contentType()!!.match(ContentType.Application.Json)) {
                serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_NOT_FOUND)
                return
            }
            when (response.status) {
                HttpStatusCode.NotFound -> {
                    serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_NOT_FOUND)
                    return
                }
                HttpStatusCode.BadRequest -> {
                    serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_BAD_REQUEST)
                    return
                }
                HttpStatusCode.Forbidden, HttpStatusCode.Unauthorized -> {
                    serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_UNAUTHORIZED)
                    return
                }
            }

            serviceCallStatus.emit(ServiceCallStatus.FINISHED_SUCCESS)
            return
        } catch (ex: Exception) {
            if (ex is ConnectException) {
                serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_CONNECTION)
                return
            } else if (ex is HttpRequestTimeoutException) {
                serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_TIMEOUT)
                return
            }
            Log.e("accountEnrollment", "${ex.message}")
            serviceCallStatus.emit(ServiceCallStatus.FINISHED_ERROR_UNKNOWN)
        }
    }
}