package net.nomia.pos.ui.auth.pos_setup.mvi


import net.nomia.mvi.Event
import net.nomia.mvi.MviFeatureFactory

internal class PosSetupMviFeatureFactory(
    bootstrap: PosSetupMviBootstrap,
    actor: PosSetupMviActor,
) : MviFeatureFactory<PosSetupMviAction, PosSetupMviEffect, PosSetupMviState, Event>(
    initialState = PosSetupMviState.INITIAL,
    bootstrap = bootstrap,
    actor = actor,
    eventProducer = PosSetupMviEventProducer,
    reducer = PosSetupMviReducer,
    tagPostfix = "PosSetupMviFeature"
)
