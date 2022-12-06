package red.tetracube.tetracubeapp.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import red.tetracube.tetracubeapp.R

internal object TypefaceTokens {
    val WeightBold = FontWeight.Bold
    val WeightMedium = FontWeight.Medium
    val WeightRegular = FontWeight.Normal

    val Nunito = FontFamily(
        Font(R.font.nunito_regular, this.WeightRegular),
        Font(R.font.nunito_medium, this.WeightMedium),
        Font(R.font.nunito_bold, this.WeightBold)
    )
    val Brand = Nunito
    val Plain = Nunito
}