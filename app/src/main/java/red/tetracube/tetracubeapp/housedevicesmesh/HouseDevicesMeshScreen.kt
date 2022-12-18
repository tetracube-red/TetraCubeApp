package red.tetracube.tetracubeapp.housedevicesmesh

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
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
    val refreshing = houseDevicesMeshViewModel.refreshing
    val coroutineScope = rememberCoroutineScope()

    val refreshScope = rememberCoroutineScope()
    val state = rememberPullRefreshState(
        refreshing,
        {
            coroutineScope.launch {
                if (tetraCube != null) {
                    houseDevicesMeshViewModel.getHouseMeshDescription(tetraCube)
                }
            }
        }
    )

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
        serviceCallStatus = serviceCallStatus,
        refreshing = refreshing,
        refreshState = state
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HouseDevicesMeshView(
    houseName: String,
    devices: List<Device>,
    serviceCallStatus: ServiceCallStatus,
    refreshing: Boolean,
    refreshState: PullRefreshState
) {

    Column() {
        Text(
            houseName,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier
                .pullRefresh(refreshState)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (serviceCallStatus != ServiceCallStatus.CONNECTING
                    && serviceCallStatus != ServiceCallStatus.FINISHED_SUCCESS
                    && serviceCallStatus != ServiceCallStatus.IDLE
                ) {
                    item {
                        ElevatedCard(
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Text(
                                stringResource(id = R.string.devices_error_loading),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
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
                                           ) {                                           }
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

            PullRefreshIndicator(
                refreshing,
                refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}