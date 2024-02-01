package net.nomia.mvi

interface Event

data class CompositeEvent(val events: List<Event>) : Event

data class ErrorEvent(val throwable: Throwable) : Event

