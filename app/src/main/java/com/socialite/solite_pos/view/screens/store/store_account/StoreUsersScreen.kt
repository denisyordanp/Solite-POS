package com.socialite.solite_pos.view.screens.store.store_account

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.socialite.domain.menu.UserAuthority
import com.socialite.domain.schema.main.User
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAddButton
import com.socialite.solite_pos.compose.BasicAlertDialog
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.GeneralDropdown
import com.socialite.solite_pos.compose.GeneralDropdownItem
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.solite_pos.utils.config.PasswordStatus
import com.socialite.solite_pos.utils.config.isNotValidEmail
import com.socialite.solite_pos.utils.config.isNotValidPassword
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
fun StoreUsersScreen(
    onBackClicked: () -> Unit
) {
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var selectedUserForDetail by remember { mutableStateOf<User?>(null) }
    val storeAccounts = listOf(
        User(
            id = "",
            name = "Testing1",
            email = "Testing@mail.com",
            authority = UserAuthority.STAFF,
            isUserActive = true,
            password = ""
        ),
        User(
            id = "",
            name = "Testing2",
            email = "Testing2@mail.com",
            authority = UserAuthority.ADMIN,
            isUserActive = false,
            password = ""
        )
    )

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        sheetContent = {
            UserDetail(
                user = selectedUserForDetail,
                onSubmitUser = {},
                errorMessage = ""
            )
        },
        content = {
            Scaffold(
                topBar = {
                    BasicTopBar(
                        titleText = stringResource(id = R.string.store_users),
                        onBackClicked = onBackClicked
                    )
                },
                content = { padding ->
                    StoresUsersContent(
                        modifier = Modifier
                            .padding(padding),
                        storeAccounts = storeAccounts,
                        onAddClicked = {
                            scope.launch {
                                selectedUserForDetail = null
                                modalState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
                        onAccountClicked = {
                            scope.launch {
                                selectedUserForDetail = it
                                modalState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
                        onUserSwitched = { user, switch ->
                            //TODO: update user access
                        }
                    )
                }
            )
        }
    )
}

@Composable
private fun StoresUsersContent(
    modifier: Modifier = Modifier,
    storeAccounts: List<User>,
    onAddClicked: () -> Unit,
    onAccountClicked: (User) -> Unit,
    onUserSwitched: (User, Boolean) -> Unit
) {
    var alertSwitchedUser by remember { mutableStateOf<Pair<User, Boolean>?>(null) }

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
            items(storeAccounts) { user ->
                UserItem(
                    user = user,
                    onUserClicked = onAccountClicked,
                    onUserSwitched = {
                        alertSwitchedUser = Pair(user, it)
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

    alertSwitchedUser?.let {
        val title = if (it.second) R.string.user_access_on else R.string.user_access_off
        val message = if (it.second) R.string.user_access_on_desc else R.string.user_access_off_desc
        val userName = it.first.name
        val messageText = stringResource(id = message, userName)

        val start = messageText.indexOf(userName)
        val spanStyles = listOf(
            AnnotatedString.Range(
                SpanStyle(fontWeight = FontWeight.Bold),
                start = start,
                end = start + userName.length
            )
        )
        BasicAlertDialog(
            titleText = stringResource(title),
            descAnnotatedString = AnnotatedString(text = messageText, spanStyles = spanStyles),
            positiveAction = {
                onUserSwitched(it.first, it.second)
                alertSwitchedUser = null
            },
            positiveText = stringResource(R.string.yes),
            negativeAction = {
                alertSwitchedUser = null
            },
            negativeText = stringResource(R.string.no)
        )
    }
}

@Composable
private fun UserItem(
    user: User,
    onUserClicked: (User) -> Unit,
    onUserSwitched: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onUserClicked(user)
            }
            .background(color = MaterialTheme.colors.surface)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.body2,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.authority.name,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Switch(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            checked = user.isUserActive,
            onCheckedChange = onUserSwitched,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.primaryVariant
            )
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
private fun UserDetail(
    user: User?,
    errorMessage: String?,
    onSubmitUser: (User) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var authority by remember { mutableStateOf<UserAuthority?>(null) }

    LaunchedEffect(key1 = user) {
        name = user?.name ?: ""
        email = user?.email ?: ""
        authority = user?.authority
    }

    val nameErrorMessage by remember {
        derivedStateOf {
            when {
                name.isEmpty() -> R.string.field_can_not_emtpy
                else -> null
            }
        }
    }
    val emailErrorMessage by remember {
        derivedStateOf {
            when {
                email.isEmpty() -> R.string.field_can_not_emtpy
                email.isNotValidEmail() -> R.string.fill_with_email_correct_format
                else -> null
            }
        }
    }
    val passwordErrorMessage by remember {
        derivedStateOf {
            when {
                password.isEmpty() -> R.string.field_can_not_emtpy
                else -> when (password.isNotValidPassword()) {
                    PasswordStatus.LENGTH -> R.string.password_must_between_8_15_character
                    PasswordStatus.UPPER_LOWER_CASE -> R.string.password_must_contains_upper_lower_case
                    PasswordStatus.SYMBOL -> R.string.password_must_contains_symbol
                    PasswordStatus.SUCCESS -> null
                }
            }
        }
    }
    val confirmPasswordErrorMessage by remember {
        derivedStateOf {
            when {
                confirmPassword.isEmpty() -> R.string.field_can_not_emtpy
                confirmPassword != password -> R.string.confirm_password_must_match_with_password
                else -> null
            }
        }
    }
    val authorityErrorMessage by remember {
        derivedStateOf {
            when (authority) {
                null -> R.string.field_can_not_emtpy
                else -> null
            }
        }
    }
    val isButtonEnabled by remember {
        derivedStateOf {
            emailErrorMessage == null &&
                    passwordErrorMessage == null &&
                    authorityErrorMessage == null &&
                    nameErrorMessage == null
        }
    }

    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.add_store_user),
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        BasicEditText(
            value = name,
            placeHolder = stringResource(R.string.enter_user_name),
            onValueChange = {
                name = it
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        nameErrorMessage?.let {
            Text(
                text = stringResource(id = it),
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        BasicEditText(
            value = email,
            keyboardType = KeyboardType.Email,
            placeHolder = stringResource(R.string.enter_email),
            onValueChange = {
                email = it
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        emailErrorMessage?.let {
            Text(
                text = stringResource(id = it),
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        GeneralDropdown(
            label = stringResource(R.string.select_authority),
            items = UserAuthority.values().map {
                   GeneralDropdownItem(it.name, it)
            },
            selectedItem = authority?.run { GeneralDropdownItem(this.name, this) },
            onSelectedItem = {
                authority = it.value
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        authorityErrorMessage?.let {
            Text(
                text = stringResource(id = it),
                color = Color.Red
            )
        }
        if (user == null) {
            Spacer(modifier = Modifier.height(16.dp))
            BasicEditText(
                value = password,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                passwordVisible = passwordVisibility,
                placeHolder = stringResource(R.string.enter_password),
                onValueChange = {
                    password = it
                },
                onPasswordVisibility = {
                    passwordVisibility = !passwordVisibility
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            passwordErrorMessage?.let {
                Text(
                    text = stringResource(id = it),
                    color = Color.Red
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            BasicEditText(
                value = confirmPassword,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                passwordVisible = confirmPasswordVisibility,
                placeHolder = stringResource(R.string.enter_confirm_password),
                onValueChange = {
                    confirmPassword = it
                },
                onPasswordVisibility = {
                    confirmPasswordVisibility = !confirmPasswordVisibility
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            confirmPasswordErrorMessage?.let {
                Text(
                    text = stringResource(id = it),
                    color = Color.Red
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (errorMessage.isNullOrEmpty().not()) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(
                        color = MaterialTheme.colors.surface.copy(
                            0.3f
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = errorMessage!!,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        val buttonText = if (user != null) R.string.save else R.string.adding
        PrimaryButtonView(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.surface,
            isEnabled = isButtonEnabled,
            buttonText = stringResource(id = buttonText),
            onClick = {
                onSubmitUser(
                    user?.copy(
                        name = name,
                        email = email,
                        authority = authority!!
                    ) ?: User.add(
                        name = name,
                        email = email,
                        authority = authority!!,
                        password = password
                    )
                )
                name = ""
                email = ""
                authority = UserAuthority.OWNER
                password = ""
                confirmPassword = ""
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
@Preview(showSystemUi = true)
private fun Preview() {
    SolitePOSTheme {
        StoreUsersScreen {

        }
    }
}
