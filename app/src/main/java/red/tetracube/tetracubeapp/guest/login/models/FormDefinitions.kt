package red.tetracube.tetracubeapp.guest.login.models

import red.tetracube.tetracubeapp.R

enum class FormFieldName(val labelId: Int) {
    NICKNAME(R.string.nickname),
    PASSWORD(R.string.password),
    TETRACUBE_HOST_ADDRESS(R.string.tetracube_host_address)
}