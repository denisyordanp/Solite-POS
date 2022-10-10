package com.socialite.solite_pos.view.main.opening.store

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.compose.GeneralMenusView
import com.socialite.solite_pos.view.main.opening.ui.GeneralMenus
import com.socialite.solite_pos.view.main.opening.ui.MasterMenus
import com.socialite.solite_pos.view.main.opening.ui.ModalContent
import com.socialite.solite_pos.view.main.opening.ui.StoreMenus
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
fun MainStoreMenu(
    onGeneralMenuClicked: (menu: GeneralMenus) -> Unit,
    onMasterMenuClicked: (menu: MasterMenus) -> Unit,
    onStoreMenuClicked: (menu: StoreMenus) -> Unit
) {
    var modalContent by remember {
        mutableStateOf(ModalContent.MASTERS)
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
                ModalContent.MASTERS -> {
                    MasterMenusView(onMenuClicked = onMasterMenuClicked)
                }

                ModalContent.GENERAL_MENUS -> GeneralMenusView(
                    onClicked = {
                        if (it == GeneralMenus.STORE) {
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
            StoreMenus(
                onGeneralMenuClicked = {
                    scope.launch {
                        modalContent = ModalContent.GENERAL_MENUS
                        modalState.show()
                    }
                },
                onStoreMenuClicked = {
                    if (it == StoreMenus.MASTERS) {
                        scope.launch {
                            modalContent = ModalContent.MASTERS
                            modalState.show()
                        }
                    } else {
                        onStoreMenuClicked(it)
                    }
                }
            )
        }
    )
}
