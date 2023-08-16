package com.socialite.solite_pos.view.screens.store.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAddButton
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.solite_pos.data.schema.room.new_master.Category
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
fun CategoryMasterScreen(
    currentViewModel: CategoryMasterViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val categories =
        currentViewModel.getCategories(Category.getFilter(Category.ALL)).collectAsState(
            initial = emptyList()
        ).value
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    if (modalState.currentValue == ModalBottomSheetValue.Hidden) {
        LocalSoftwareKeyboardController.current?.hide()
    }

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        sheetContent = {
            CategoryDetail(
                category = selectedCategory,
                onSubmitCategory = {
                    scope.launch {
                        if (it.isNewCategory()) {
                            currentViewModel.insertCategory(it)
                        } else {
                            currentViewModel.updateCategory(it)
                        }
                        selectedCategory = null
                        modalState.hide()
                    }
                }
            )
        },
        content = {
            Scaffold(
                topBar = {
                    BasicTopBar(
                        titleText = stringResource(id = R.string.categories),
                        onBackClicked = onBackClicked
                    )
                },
                content = { padding ->
                    CategoryContent(
                        modifier = Modifier
                            .padding(padding),
                        categories = categories,
                        onCategoryClicked = {
                            scope.launch {
                                selectedCategory = it
                                modalState.show()
                            }
                        },
                        onAddClicked = {
                            scope.launch {
                                selectedCategory = null
                                modalState.show()
                            }
                        },
                        onSwitched = {
                            currentViewModel.updateCategory(it)
                        }
                    )
                }
            )
        }
    )
}

@Composable
@ExperimentalComposeUiApi
private fun CategoryDetail(
    category: Category?,
    onSubmitCategory: (Category) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    LaunchedEffect(key1 = category) {
        name = category?.name ?: ""
        desc = category?.desc ?: ""
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        BasicEditText(
            value = name,
            placeHolder = stringResource(R.string.category_name),
            onValueChange = {
                name = it
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicEditText(
            value = desc,
            placeHolder = stringResource(R.string.deskripsi_optional),
            onValueChange = {
                desc = it
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButtonView(
            modifier = Modifier
                .fillMaxWidth(),
            buttonText = stringResource(
                id = if (category == null) R.string.adding else R.string.save
            ),
            onClick = {
                if (name.isNotEmpty()) {
                    onSubmitCategory(
                        category?.copy(
                            name = name,
                            desc = desc
                        ) ?: Category.createNewCategory(
                            name = name,
                            desc = desc
                        )
                    )
                    name = ""
                    desc = ""
                }
            }
        )
    }
}

@Composable
private fun CategoryContent(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onCategoryClicked: (Category) -> Unit,
    onAddClicked: () -> Unit,
    onSwitched: (category: Category) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        val listState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .align(Alignment.TopCenter),
            state = listState
        ) {
            items(categories) {
                CategoryItem(
                    category = it,
                    onCategoryClicked = onCategoryClicked,
                    onCategorySwitched = { isActive ->
                        onSwitched(
                            it.copy(
                                isActive = isActive
                            )
                        )
                    }
                )
            }

            item { SpaceForFloatingButton() }
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            visible = !listState.isScrollInProgress,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            BasicAddButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                onAddClicked = onAddClicked
            )
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    onCategoryClicked: (Category) -> Unit,
    onCategorySwitched: (isActive: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCategoryClicked(category)
            }
            .background(color = MaterialTheme.colors.surface)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = category.desc,
                style = MaterialTheme.typography.body2
            )
        }
        Switch(
            checked = category.isActive,
            onCheckedChange = onCategorySwitched,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.primaryVariant
            )
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}
