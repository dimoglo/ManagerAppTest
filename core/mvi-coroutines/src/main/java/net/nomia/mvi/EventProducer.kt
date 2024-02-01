package net.nomia.mvi

/**
 * Implement this interface to listen for final Effects (after applying EffectInterceptor)
 *
 * Doesn't operate on particular scheduler. invoke() can be called on any thread
 *
 */
fun interface EventProducer<Effect : Any, Event : Any?> : (Effect) -> Event? {
    override fun invoke(effect: Effect): Event?
}
