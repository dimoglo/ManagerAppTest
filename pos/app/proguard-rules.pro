# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
# -keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# General
-keepattributes SourceFile,LineNumberTable,*Annotation*,EnclosingMethod,Signature,Exceptions,InnerClasses
-allowaccessmodification

# custom view
-keep class com.google.android.material.textfield.TextInputLayout { *;}
-keep class com.google.android.material.internal.CollapsingTextHelper { *; }

# validator
-keep class com.mobsandgeeks.saripaar.** {*;}
-keep @com.mobsandgeeks.saripaar.annotation.ValidateUsing class * {*;}

-keep class * extends com.mobsandgeeks.saripaar.adapter.ViewDataAdapter {*;}

-keepclassmembers class ** {
    @com.mobsandgeeks.saripaar.annotation.* *;
}

# Jackson
-keep @com.fasterxml.jackson.annotation.JsonIgnoreProperties class * { *; }
-keep @com.fasterxml.jackson.annotation.JsonCreator class * { *; }
-keep @com.fasterxml.jackson.annotation.JsonValue class * { *; }

-keep class com.fasterxml.** { *; }
-keep class org.codehaus.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepclassmembers public final enum com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility {
    public static final com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility *;
}

-keepclasseswithmembernames class * {
    @com.fasterxml.jackson.annotation.JsonProperty <methods>;
    @com.fasterxml.jackson.annotation.JsonProperty <fields>;
}

# data model
-keep class net.nomia.pos.core.dto.** { *; }
-keep class net.nomia.pos.device_server.entity.** { *; }

# device server
-keep class net.nomia.integration.kkt.** { *; }
-keep class net.nomia.pos.device_server.service.DeviceServerManager {*;}
#-keep class net.nomia.pos.device_server.service.DeviceForegroundService {*; }
-keep class net.nomia.pos.config.AtolModule {*; }
-keep class ru.atol.drivers10.** {*;}
-keep class net.nomia.integration.kkt.core.service.** {*;}
#-keep class * extends net.nomia.integration.kkt.core.service.** {
#    <methods>;
#}
# -keep class net.nomia.pos.device_server.service.DeviceForegroundService { *; }

# carbon
-dontwarn carbon.BR
-dontwarn carbon.internal**
-dontwarn java.lang.invoke**

-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
#-keep class carbon.** { *; }

# keep view animation
#-keep class android.view.View { *; }
-keep class net.nomia.pos.dashboard.** { *; }
-keep class net.nomia.pos.core.model.** { *; }

# Kotlin serializer
# https://github.com/Kotlin/kotlinx.serialization/issues/1121
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
}
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclasseswithmembers class **$$serializer {
    *** INSTANCE;
}

-keep class net.nomia.pos.core.data.** { *; }
-keep class net.nomia.hardware.atol.** { *; }
-keep class net.nomia.integration.kkt.** { *; }
-keep class ru.atol.drivers10.** {*;}
-keep class net.nomia.integration.kkt.core.service.** {*;}
-keep class net.nomia.hardware.core.** { *; }
-keep class net.nomia.hardware.core.domain.** { *; }
-keep class tech.units.indriya.** { *; }

-dontwarn java.beans.ConstructorProperties
-dontwarn java.beans.Transient
-dontwarn java.util.spi.LocaleServiceProvider
-dontwarn javax.servlet.ServletContainerInitializer
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

# to use arguments with SavedStateHandle.getArguments()
-keepclasseswithmembers class **.*DestinationArgs {
    <init>(...);
    *;
}

# Keep kotlin.Metadata annotations to maintain metadata on kept items.
-keepattributes RuntimeVisibleAnnotations
-keep class kotlin.Metadata { *; }
