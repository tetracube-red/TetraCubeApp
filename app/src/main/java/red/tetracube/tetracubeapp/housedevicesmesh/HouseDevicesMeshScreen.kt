package red.tetracube.tetracubeapp.housedevicesmesh

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import red.tetracube.tetracubeapp.R
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.core.extensions.color
import red.tetracube.tetracubeapp.core.settings.PairedTetraCube
import red.tetracube.tetracubeapp.housedevicesmesh.models.Device

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HouseDevicesMeshScreen(
    houseDevicesMeshViewModel: HouseDevicesMeshViewModel = viewModel(),
    tetraCube: PairedTetraCube?,
) {
    val serviceCallStatus = houseDevicesMeshViewModel.serviceCallStatus
    val devices = houseDevicesMeshViewModel.devices
    val houseName = houseDevicesMeshViewModel.houseName

    LaunchedEffect(
        key1 = Unit,
        block = {
            if (tetraCube != null) {
                houseDevicesMeshViewModel.getHouseMeshDescription(tetraCube)
            }
        }
    )

    HouseDevicesMeshView(
        houseName = houseName,
        devices = devices,
        serviceCallStatus = serviceCallStatus
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDevicesMeshView(
    houseName: String,
    devices: List<Device>,
    serviceCallStatus: ServiceCallStatus
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column() {
                    Image(
                        contentScale = ContentScale.FillHeight,
                        painter = painterResource(id = R.drawable.smart_home),
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column() {
                    Text(
                        houseName,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        "55 devices",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (serviceCallStatus != ServiceCallStatus.CONNECTING
                && serviceCallStatus != ServiceCallStatus.FINISHED_SUCCESS
                && serviceCallStatus != ServiceCallStatus.IDLE
            ) {
                item {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text(
                            stringResource(id = R.string.devices_error_loading),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )

                        TextButton(onClick = { /*TODO*/ }) {
                            Text(stringResource(id = R.string.devices_error_loading_do_refresh),)
                        }
                    }
                }
            }
            items(devices) { device ->
                ListItem(
                    headlineText = { Text(device.name) },
                    supportingText = { Text("@ ${device.environment.name}") },
                    overlineText = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(size = 10.dp)
                                    .clip(shape = CircleShape)
                                    .background(color = device.connectionStatusColor),
                                contentAlignment = Alignment.Center,
                            ) { }
                            Spacer(Modifier.width(4.dp))
                            Text(text = if (device.isOnline) "is online" else "is offline")
                        }
                    },
                    leadingContent = {
                        Box(
                            modifier = Modifier
                                .size(size = 50.dp)
                                .clip(shape = RoundedCornerShape(size = 12.dp))
                                .background(color = device.colorCode.color),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(
                                painter = painterResource(id = device.deviceTypeIcon),
                                contentDescription = null,
                            )
                        }
                    },
                    trailingContent = {
                        DeviceItemTrailingWidget(device = device)
                    }
                )
                Divider()
            }
        }
    }
}