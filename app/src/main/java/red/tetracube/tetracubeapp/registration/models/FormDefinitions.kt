package red.tetracube.tetracubeapp.registration.models

import red.tetracube.tetracubeapp.R

enum class FormDataFieldName(val labelId: Int) {
    USERNAME(R.string.username),
    PASSWORD(R.string.password),
    TETRACUBE_HOST_ADDRESS(R.string.tetracube_host_address)
}