package red.tetracube.tetracubeapp.core.definitions

enum class ServiceCallStatus {
    IDLE,
    CONNECTING,
    FINISHED_SUCCESS,
    FINISHED_ERROR_UNKNOWN,
    FINISHED_ERROR_CONNECTION,
    FINISHED_ERROR_NOT_FOUND,
    FINISHED_ERROR_UNAUTHORIZED,
    FINISHED_ERROR_TIMEOUT,
    FINISHED_CONFLICTING,
    FINISHED_INVALID_TOKEN
}