package com.socialite.solite_pos.view.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.GeneralMenuButtonView
import com.socialite.solite_pos.compose.GeneralMenusView
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.ModalContent
import com.socialite.solite_pos.view.ui.SettingMenus
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
fun SettingsMainMenu(
    orderViewModel: OrderViewModel,
    mainViewModel: MainViewModel,
    currentDate: String,
    onGeneralMenuClicked: (menu: GeneralMenus) -> Unit,
    onDarkModeChange: (Boolean) -> Unit,
    onDeveloperClicked: () -> Unit,
) {
    var modalContent by remember {
        mutableStateOf(ModalContent.GENERAL_MENUS)
    }
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp
        ),
        sheetContent = {
            when (modalContent) {
                ModalContent.GENERAL_MENUS -> GeneralMenusView(
                    orderViewModel = orderViewModel,
                    date = currentDate,
                    onClicked = {
                        if (it == GeneralMenus.SETTING) {
                            scope.launch {
                                modalState.hide()
                            }
                        } else {
                            onGeneralMenuClicked(it)
                        }
                    }
                )

                else -> {
                    // Do nothing
                }
            }
        },
        content = {
            SettingsMenus(
                mainViewModel = mainViewModel,
                onGeneralMenuClicked = {
                    scope.launch {
                        modalContent = ModalContent.GENERAL_MENUS
                        modalState.show()
                    }
                },
                onDarkModeChange = onDarkModeChange,
                onDeveloperClicked = onDeveloperClicked
            )
        }
    )
}

@Composable
fun SettingsMenus(
    mainViewModel: MainViewModel,
    onGeneralMenuClicked: () -> Unit,
    onDeveloperClicked: () -> Unit,
    onDarkModeChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            items(SettingMenus.values()) {
                when (it) {
                    SettingMenus.THEME -> ThemeSettingMenu(mainViewModel, onDarkModeChange)
                    SettingMenus.DEVELOPER -> DeveloperSettingMenu(onDeveloperClicked = onDeveloperClicked)
                    SettingMenus.SYNCHRONIZE -> SynchronizationMenu(mainViewModel)
                }
            }
        }
        GeneralMenuButtonView(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onMenuClicked = onGeneralMenuClicked
        )
    }
}

@Composable
fun ThemeSettingMenu(
    mainViewModel: MainViewModel,
    onDarkModeChange: (Boolean) -> Unit
) {

    val isDarkMode = mainViewModel.isDarkModeActive.collectAsState(initial = false)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface
            )
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            text = stringResource(id = SettingMenus.THEME.title),
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colors.onSurface
        )
        Switch(
            checked = isDarkMode.value,
            onCheckedChange = {
                mainViewModel.setDarkMode(it)
                onDarkModeChange(it)
            }
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun DeveloperSettingMenu(
    onDeveloperClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface
            )
            .clickable {
                onDeveloperClicked()
            }
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            text = stringResource(id = SettingMenus.DEVELOPER.title),
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colors.onSurface
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun SynchronizationMenu(
    mainViewModel: MainViewModel,
) {
    Spacer(modifier = Modifier.height(8.dp))
    PrimaryButtonView(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        buttonText = stringResource(id = R.string.synchronize)
    ) {
        mainViewModel.beginSynchronize()
    }
}
