package red.tetracube.tetracubeapp.userlogin.models

import red.tetracube.tetracubeapp.R

enum class FormFieldName(val labelId: Int) {
    USERNAME(R.string.username),
    AUTHENTICATION_CODE(R.string.authentication_code),
    TETRACUBE_HOST_ADDRESS(R.string.tetracube_host_address)
}