package red.tetracube.tetracubeapp.housedevicesmesh

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import red.tetracube.tetracubeapp.core.extensions.color
import red.tetracube.tetracubeapp.core.settings.PairedTetraCube
import red.tetracube.tetracubeapp.core.settings.TetraCubeSettings
import red.tetracube.tetracubeapp.housedevicesmesh.models.Device

@Composable
fun HouseDevicesMeshScreen(
    houseDevicesMeshViewModel: HouseDevicesMeshViewModel = viewModel(),
    tetraCube: PairedTetraCube?,
) {
    val serviceCallStatus = houseDevicesMeshViewModel.serviceCallStatus
    val devices = houseDevicesMeshViewModel.devices
    LaunchedEffect(
        key1 = Unit,
        block = {
            if (tetraCube != null) {
                houseDevicesMeshViewModel.getHouseMeshDescription(tetraCube)
            }
        }
    )

    HouseDevicesMeshView(
        devices = devices
    )
}

@Composable
fun HouseDevicesMeshView(
    devices: List<Device>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(devices) { device ->
            ElevatedCard {
                Image(
                    painter = painterResource(id = device.deviceTypeIcon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = device.colorCode.color),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(color = device.connectionStatusColor),
                )
                Column(
                    modifier = Modifier.padding(8.dp),
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = device.name, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "@ ${device.environment.name}", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}