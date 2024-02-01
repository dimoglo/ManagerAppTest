package net.nomia.pos.ui.auth.pos_setup

import dagger.hilt.android.lifecycle.HiltViewModel
import net.nomia.mvi.MviViewModel
import net.nomia.pos.ui.auth.pos_setup.mvi.PosSetupMviAction
import net.nomia.pos.ui.auth.pos_setup.mvi.PosSetupMviFeatureFactory
import net.nomia.pos.ui.auth.pos_setup.mvi.PosSetupMviState
import javax.inject.Inject

@HiltViewModel
internal class PosSetupViewModel @Inject constructor(
    factory: PosSetupMviFeatureFactory,
) : MviViewModel<PosSetupMviState, PosSetupMviAction>(factory)
