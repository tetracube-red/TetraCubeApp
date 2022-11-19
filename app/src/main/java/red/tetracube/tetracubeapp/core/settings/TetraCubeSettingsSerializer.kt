package red.tetracube.tetracubeapp.core.settings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object TetraCubeSettingsSerializer : Serializer<TetraCubeSettings> {
    override val defaultValue: TetraCubeSettings = TetraCubeSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TetraCubeSettings =
        try {
            TetraCubeSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(
        t: TetraCubeSettings,
        output: OutputStream
    ) = t.writeTo(output)
}