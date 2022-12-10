package red.tetracube.tetracubeapp.housedevicesmesh

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.core.extensions.color
import red.tetracube.tetracubeapp.core.settings.PairedTetraCube
import red.tetracube.tetracubeapp.housedevicesmesh.models.Device
import red.tetracube.tetracubeapp.R

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

@OptIn(ExperimentalMaterialApi::class)
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

        Box(modifier = Modifier.pullRefresh(refreshState).fillMaxWidth().fillMaxHeight()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                    ElevatedCard {
                        Box(
                            modifier = Modifier
                                .background(color = device.colorCode.color)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = device.deviceTypeIcon),
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .height(48.dp),
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .background(color = device.connectionStatusColor),
                        )
                        Column(
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(text = device.name, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "@ ${device.environment.name}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Last update: 29/44 at 11:44",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
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