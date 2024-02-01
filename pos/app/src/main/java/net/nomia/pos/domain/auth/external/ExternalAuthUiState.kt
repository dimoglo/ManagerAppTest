package net.nomia.pos.domain.auth.external

import androidx.annotation.StringRes
import net.nomia.common.data.model.SignInResponse
import net.nomia.pos.R
import net.nomia.pos.core.text.Content

sealed interface ExternalAuthUiState {
    @get:StringRes
    val titleResId: Int

    @get:StringRes
    val subtitleResId: Int

    data class Welcome(
        val loading: Boolean = false,
        val errorMessage: Content? = null,
        override val titleResId: Int = R.string.welcome_title,
        override val subtitleResId: Int = R.string.auth_subtitle,
    ) : ExternalAuthUiState

    sealed interface Login : ExternalAuthUiState {
        val signIn: SignInResponse
        val loading: Boolean
        val errorMessage: Content?

        data class Phone(
            override val signIn: SignInResponse,
            override val loading: Boolean = false,
            override val errorMessage: Content? = null,
            override val titleResId: Int = R.string.input_code_from_phone,
            override val subtitleResId: Int = R.string.input_code_from_phone_hint,
        ) : Login

        data class Email(
            override val signIn: SignInResponse,
            override val loading: Boolean = false,
            override val errorMessage: Content? = null,
            override val titleResId: Int = R.string.input_code_from_email,
            override val subtitleResId: Int = R.string.input_code_from_email_hint,
        ) : Login

        data class Password(
            override val signIn: SignInResponse,
            override val loading: Boolean = false,
            override val errorMessage: Content? = null,
            override val titleResId: Int = R.string.input_password,
            override val subtitleResId: Int = R.string.input_password_for,
        ) : Login

        companion object {
            const val CodeSize = 4
        }
    }

    data class ExternalActionRequired(
        override val titleResId: Int,
        override val subtitleResId: Int,
        val buttonTitleResId: Int,
    ) : ExternalAuthUiState
}
