package red.tetracube.tetracubeapp.codescanner

import red.tetracube.tetracubeapp.core.routing.ScreenRoute

object CameraQRScannerScreenRoute : ScreenRoute {
    override val route: String = "qr-scanner"
    override val showAppBar: Boolean = true
    override val screenTitle: String = "QR Code Scanner"
    override val showBottomBar: Boolean = false
}