package net.nomia.pos.domain.auth.pos_setup.use_cases

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import net.nomia.main.domain.ErpLoginRepository
import net.nomia.main.domain.ErpTerminalRepository
import net.nomia.main.domain.toDomain
import net.nomia.settings.getDeviceType
import net.nomia.pos.R
import net.nomia.pos.domain.auth.pos_setup.TerminalState
import net.nomia.pos.domain.auth.pos_setup.TerminalValue
import net.nomia.pos.ui.auth.pos_setup.toInput
import net.nomia.settings.domain.SettingsRepository
import javax.inject.Inject

interface SaveTerminalUseCase {

    fun save(value: TerminalValue) : Flow<TerminalState>
}

class SaveTerminalUseCaseImpl @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val erpTerminalRepository: ErpTerminalRepository,
    private val erpLoginRepository: ErpLoginRepository,
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context,
) : SaveTerminalUseCase {


    @Suppress("TooGenericExceptionCaught")
    override fun save(value: TerminalValue): Flow<TerminalState> = flow {
        val auth = authUseCase.get()

        val terminalInput = value.toInput()

        if (auth != null && terminalInput != null) {

            emit(TerminalState.Processing)

            try {
                val terminalId = erpTerminalRepository.save(terminalInput, auth).first()

                val terminal = terminalInput
                    .copy(id = terminalId)
                    .toDomain(deviceType = context.getDeviceType())
                settingsRepository.saveTerminal(terminal)
                settingsRepository.setDefaultTerminal(terminal)

                val token = erpLoginRepository.createApplicationToken(auth, terminal).first()
                settingsRepository.saveApplicationToken(token)
                emit(TerminalState.Saved)
            } catch (e: Exception) {
                emit(TerminalState.Error(e.message ?: context.getString(R.string.failed_to_save)))
            }
        } else {
            emit(TerminalState.Error(context.getString(R.string.something_went_wrong)))
        }

    }

}
