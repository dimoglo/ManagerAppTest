package net.nomia.erp.api

import android.app.Application
import android.content.pm.PackageManager
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.subscription.SubscriptionConnectionParams
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import com.auth0.android.jwt.JWT
import io.sentry.apollo.SentryApolloInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import net.nomia.common.data.model.Organization
import net.nomia.erp.api.adapter.BigDecimalAdapter
import net.nomia.erp.api.adapter.InstantAdapter
import net.nomia.erp.api.adapter.LocalDateAdapter
import net.nomia.erp.api.adapter.LocalDateTimeAdapter
import net.nomia.erp.api.adapter.LocalTimeAdapter
import net.nomia.erp.api.adapter.UUIDAdapter
import net.nomia.erp.schema.type.CustomType
import net.nomia.settings.domain.SettingsRepository
import net.nomia.settings.domain.model.ServerProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErpClientService @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val app: Application,
    scope: CoroutineScope
) {

    private val application: Flow<ApolloClient?> = combine(
        settingsRepository.getServerProvider(),
        settingsRepository.getOrganizationId(),
        settingsRepository.getApplicationToken()
    ) { provider, organizationId, token ->
        if (organizationId != null && token != null) {
            val okHttpClient = okHttpClientBuilder()
                .addInterceptor(AuthorizationInterceptor(token.accessToken))
                .addInterceptor(OrganizationInterceptor(organizationId))
                .build()
            return@combine apolloClientBuilder(provider, okHttpClient)
                .subscriptionTransportFactory(
                    WebSocketSubscriptionTransport.Factory(
                        provider.wss,
                        okHttpClient
                    )
                )
                .subscriptionConnectionParams(
                    SubscriptionConnectionParams(
                        mutableMapOf(
                            "Authorization" to "Bearer ${token.accessToken}",
                            "OrganizationId" to organizationId.value
                        )
                    )
                ).build()
        }

        null
    }.stateIn(scope, SharingStarted.Eagerly, null)

    fun applicationClient() = application

    fun principalClient(
        token: JWT? = null,
        refreshToken: JWT? = null,
        organizationId: Organization.ID? = null
    ): Flow<ApolloClient> {
        return settingsRepository.getServerProvider().map { provider ->
            val okHttpClient = okHttpClientBuilder().apply {
                if (token != null) {
                    addInterceptor(AuthorizationInterceptor(token))
                }

                if (refreshToken != null) {
                    addInterceptor(RefreshTokenInterceptor(refreshToken))
                }

                if (organizationId != null) {
                    addInterceptor(OrganizationInterceptor(organizationId))
                }
            }.build()
            apolloClientBuilder(provider, okHttpClient).build()
        }
    }

    private fun apolloClientBuilder(
        provider: ServerProvider,
        okHttpClient: OkHttpClient = okHttpClientBuilder().build()
    ): ApolloClient.Builder {
        return ApolloClient.builder()
            .okHttpClient(okHttpClient)
            .addApplicationInterceptor(SentryApolloInterceptor())
            .serverUrl(provider.api)
            .addCustomTypeAdapter(CustomType.UUID, UUIDAdapter())
            .addCustomTypeAdapter(CustomType.BIGDECIMAL, BigDecimalAdapter())
            .addCustomTypeAdapter(CustomType.INSTANT, InstantAdapter())
            .addCustomTypeAdapter(CustomType.LOCALTIME, LocalTimeAdapter())
            .addCustomTypeAdapter(CustomType.LOCALDATE, LocalDateAdapter())
            .addCustomTypeAdapter(CustomType.LOCALDATETIME, LocalDateTimeAdapter())
    }

    private fun okHttpClientBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor(
                logger = nomiaHttpLogger()
            ).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        try {
            val packageInfo =
                app.applicationContext.packageManager.getPackageInfo(app.packageName, 0)
            val appVersion = packageInfo.versionName
            builder.addInterceptor(AppInfoInterceptor(appVersion, app.packageName))
            builder.addInterceptor(AcceptLanguageInterceptor("ru"))
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.tag(TAG).w(e, "Version of application is undefined")
        }
        builder.connectTimeout(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS)
        builder.readTimeout(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS)
        builder.writeTimeout(DEFAULT_TIMEOUT_SEC, TimeUnit.SECONDS)
        return builder
    }

    private fun nomiaHttpLogger(): HttpLoggingInterceptor.Logger =
        HttpLoggingInterceptor.Logger { message ->
            if (message.length < HTTP_LOG_MAX_LINE_LENGTH) {
                Platform.get().log(message)
            } else {
                // Split by line, then ensure each line can fit into Log's maximum length.
                var i = 0
                val length = message.length
                while (i < length) {
                    var newline = message.indexOf('\n', i)
                    newline = if (newline != -1) newline else length
                    do {
                        val end = Math.min(newline, i + HTTP_LOG_MAX_LINE_LENGTH)
                        val part = message.substring(i, end)
                        Platform.get().log(part)
                        i = end
                    } while (i < newline)
                    i++
                }
            }
        }


    private class AuthorizationInterceptor(private val token: JWT) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            return chain.proceed(request)
        }
    }

    private class RefreshTokenInterceptor(private val token: JWT) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("RefreshToken", "$token")
                .build()

            return chain.proceed(request)
        }
    }

    private class OrganizationInterceptor(private val organizationId: Organization.ID) :
        Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("OrganizationId", organizationId.value.toString())
                .build()

            return chain.proceed(request)
        }
    }

    private class AppInfoInterceptor(private val appVersion: String, private val appType: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("AppVersion", appVersion)
                .addHeader("AppType", appType)
                .build()

            return chain.proceed(request)
        }
    }

    private class AcceptLanguageInterceptor(private val language: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Accept-Language", language)
                .build()

            return chain.proceed(request)
        }
    }

    companion object {
        private const val TAG = "ErpClientService"
        private const val DEFAULT_TIMEOUT_SEC = 20L
        private const val HTTP_LOG_MAX_LINE_LENGTH = 3000
    }

}
