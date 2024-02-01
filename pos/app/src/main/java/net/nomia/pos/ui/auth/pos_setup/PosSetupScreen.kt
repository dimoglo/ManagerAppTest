@file:OptIn(ExperimentalMaterial3Api::class)

package net.nomia.pos.ui.auth.pos_setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.ui.composable.ElevatedCard
import net.nomia.common.ui.composable.NomiaDropdownMenu
import net.nomia.common.ui.composable.NomiaFilledButton
import net.nomia.common.ui.composable.NomiaOutlinedTextField
import net.nomia.common.ui.composable.NomiaSpinner
import net.nomia.common.ui.theme.NomiaThemeMaterial3
import net.nomia.common.ui.theme.paddings
import net.nomia.common.ui.theme.spacers
import net.nomia.pos.R
import net.nomia.pos.core.data.Constraints.MAX_ENTITY_NAME_LENGTH
import net.nomia.pos.ui.navigation.ExternalAuthDestination
import net.nomia.pos.ui.auth.common.BaseAuthLayout
import net.nomia.pos.domain.auth.pos_setup.EnabledList
import net.nomia.pos.domain.auth.pos_setup.TerminalState
import net.nomia.pos.domain.auth.pos_setup.TerminalValue
import net.nomia.pos.domain.auth.pos_setup.emptyEnabledList
import net.nomia.pos.ui.auth.pos_setup.mvi.PosSetupMviAction
import net.nomia.pos.ui.auth.pos_setup.mvi.PosSetupMviEvent
import net.nomia.pos.ui.utils.stringValue
import net.nomia.pos.ui.navigation.getDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PosSetupMviScreen(
    navController: NavHostController,
    widthSizeClass: WindowWidthSizeClass,
    viewModel: PosSetupViewModel = hiltViewModel(),
) {
    val isCompactScreen = widthSizeClass == WindowWidthSizeClass.Compact

    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    val state by viewModel.collectAsState { event ->
        when (event) {
            /*PosSetupMviEvent.OnTerminalSaved -> navController.navigate(ManagerScreenDestination.getDestination())*/
            PosSetupMviEvent.OnLoggedOut -> navController.navigate(ExternalAuthDestination.getDestination())
            is PosSetupMviEvent.OnOpenedErpExternally -> uriHandler.openUri(event.erpUri)
        }
    }

    val (terminalState, terminalValue, organizations, stores, menus, hasNoMenusCreated) = state

    val snackbarHostState = remember { SnackbarHostState() }

    state.error?.let { errorContent ->
        viewModel.acceptAction(PosSetupMviAction.ResetError)
        val message = errorContent.stringValue()
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PosSetupTopBar(
                isCompactScreen = isCompactScreen,
                onLogout = { viewModel.acceptAction(PosSetupMviAction.Logout) },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { contentPadding ->
        BaseAuthLayout(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = contentPadding.calculateTopPadding())
        ) {
            PosSetupContent(
                onStartWork = { viewModel.acceptAction(PosSetupMviAction.SaveTerminal) },
                canFinishSetup = state.isValid(),
                organizations = organizations,
                stores = stores,
                menus = menus,
                hasNoMenusCreated = hasNoMenusCreated,
                terminalValue = terminalValue,
                terminalState = terminalState,
                onAction = viewModel::acceptAction,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PosSetupTopBar(
    isCompactScreen: Boolean,
    onLogout: () -> Unit,
) {
    if (isCompactScreen) {
        MediumTopAppBar(
            title = { Text(text = stringResource(id = R.string.create_selling_spot)) },
            actions = {
                IconButton(onClick = onLogout) {
                    Icon(painter = painterResource(id = R.drawable.ic_logout_24), contentDescription = null)
                }
            },
        )
    } else {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.create_selling_spot)) },
            actions = {
                IconButton(onClick = onLogout) {
                    Icon(painter = painterResource(id = R.drawable.ic_logout_24), contentDescription = null)
                }
            },
        )
    }
}

@Composable
private fun PosSetupContent(
    organizations: EnabledList<Organization>,
    stores: EnabledList<Store>,
    hasNoMenusCreated: Boolean,
    menus: EnabledList<Menu>,
    terminalValue: TerminalValue,
    terminalState: TerminalState,
    onAction: (PosSetupMviAction) -> Unit,
    onStartWork: () -> Unit,
    canFinishSetup: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.large)
    ) {

        NomiaDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            items = organizations,
            selectedItem = terminalValue.organization,
            onItemClick = { organization -> onAction(PosSetupMviAction.SelectOrganization(organization)) },
            convertToString = { it.displayName },
            enabled = organizations.enabled,
            label = { Text(text = stringResource(R.string.organization)) },
            itemText = { organization -> Text(text = organization.displayName) },
            onExpanded = { onAction.invoke(PosSetupMviAction.RetryOrganizationsRequestIfFailed) }
        )
        NomiaDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            items = stores,
            selectedItem = terminalValue.store,
            onItemClick = { store -> onAction(PosSetupMviAction.SelectStore(store)) },
            convertToString = { it.name },
            enabled = stores.enabled,
            label = { Text(text = stringResource(R.string.store)) },
            itemText = { store -> Text(text = store.name) },
            onExpanded = { onAction.invoke(PosSetupMviAction.RetryStoresRequestIfFailed) }
        )

        if (hasNoMenusCreated) {
            NoMenusCreatedWindow(
                onNavigateToErp = { onAction.invoke(PosSetupMviAction.NavigateToErp) },
            )
        } else {
            NomiaDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                items = menus,
                selectedItem = terminalValue.menu,
                onItemClick = { menu -> onAction(PosSetupMviAction.SelectMenu(menu)) },
                convertToString = { it.name },
                enabled = menus.enabled,
                label = { Text(text = stringResource(R.string.menu)) },
                itemText = { menu -> Text(text = menu.name) },
                onExpanded = { onAction.invoke(PosSetupMviAction.RetryMenusRequestIfFailed) }
            )

            var textState by remember { mutableStateOf(terminalValue.name) }
            if (terminalValue.mustUpdateName) {
                textState = terminalValue.name
            }

            NomiaOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = textState,
                onValueChange = { changedValue ->
                    val constrainedChangedValue = changedValue.take(MAX_ENTITY_NAME_LENGTH)
                    textState = constrainedChangedValue
                    onAction(PosSetupMviAction.ChangeTerminalName(name = constrainedChangedValue))
                },
                label = { Text(text = stringResource(id = R.string.pos_setup_terminal_name_label)) }
            )

            NomiaFilledButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = canFinishSetup,
                onClick = onStartWork
            ) {
                when (terminalState) {
                    TerminalState.Processing -> NomiaSpinner()
                    else -> Text(text = stringResource(id = R.string.start_work_action))
                }

            }
        }
    }
}

@Composable
private fun NoMenusCreatedWindow(
    onNavigateToErp: () -> Unit,
) {
    ElevatedCard(
        contentArrangement = Arrangement.spacedBy(MaterialTheme.spacers.medium)
    ) {
        Text(
            text = stringResource(R.string.pos_setup_no_menus_created_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = stringResource(R.string.pos_setup_no_menus_created_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToErp,
        ) {
            Text(text = stringResource(R.string.pos_setup_no_menus_created_button_title))
        }
    }
}

@Preview
@Composable
private fun PosSetupScreenPreview() {
    NomiaThemeMaterial3 {
        Surface {
            BaseAuthLayout {
                PosSetupContent(
                    onStartWork = {},
                    canFinishSetup = false,
                    organizations = emptyEnabledList(true),
                    stores = emptyEnabledList(false),
                    menus = emptyEnabledList(false),
                    hasNoMenusCreated = false,
                    terminalValue = TerminalValue(
                        id = null,
                        name = "",
                        organization = null,
                        store = null,
                        menu = null,
                        orderSequence = 0
                    ),
                    onAction = {},
                    terminalState = TerminalState.Empty,
                )
            }
        }
    }

}

val Organization.displayName : String
    @Composable get() {
        val name = this.name
        return if (name?.isNotBlank() == true) {
            name
        } else {
            stringResource(R.string.organization_id_template, code.value)
        }
    }
