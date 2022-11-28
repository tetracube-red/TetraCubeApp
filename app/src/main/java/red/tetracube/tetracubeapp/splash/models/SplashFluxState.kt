package red.tetracube.tetracubeapp.splash.models

import red.tetracube.tetracubeapp.R

enum class SplashFluxState (val resourceId: Int) {
    START(R.string.start),
    CHECKING_CONFIGURATION(R.string.checking_configuration),
    SUCCESS(R.string.success),
    ERROR(R.string.error),
    MISSING_CONFIGURATION(R.string.missing_configuration),
    LOGGING_IN(R.string.logging_in),
    GETTING_TETRACUBE_METADATA(R.string.getting_tetracube_metadata)
}