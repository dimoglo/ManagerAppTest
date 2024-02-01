package net.nomia.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import net.nomia.settings.domain.SettingsRepository
import java.util.Currency
import javax.inject.Inject

interface ObserveCurrencyUseCase {
    operator fun invoke(): Flow<Currency>
}

internal class ObserveCurrencyUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ObserveCurrencyUseCase {

    override fun invoke(): Flow<Currency> = settingsRepository.getCurrency().filterNotNull()

}
