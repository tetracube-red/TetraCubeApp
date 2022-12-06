package red.tetracube.tetracubeapp.registration.models

import red.tetracube.tetracubeapp.R

enum class FormDataFieldName(val labelId: Int) {
    USERNAME(R.string.username),
    AUTHENTICATION_CODE(R.string.authentication_code),
    TETRACUBE_HOST_ADDRESS(R.string.tetracube_host_address)
}