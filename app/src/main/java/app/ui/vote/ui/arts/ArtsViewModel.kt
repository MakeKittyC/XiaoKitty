package app.ui.vote.ui.arts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.compile.R

class ArtsViewModel : ViewModel() {

    private val _artItems = MutableLiveData<List<ArtItem>>()
    val artItems: LiveData<List<ArtItem>> get() = _artItems

    init {
        loadArtItems()
    }

    private fun loadArtItems() {
        val items = listOf(
            ArtItem(
                title = "Kotlin",
                imageResId = R.drawable.ic_xyn_kotlin,
                url = "URL: https://github.com/JetBrains/kotlin/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "KSP",
                imageResId = R.drawable.ic_xyn_kts,
                url = "URL: https://github.com/google/ksp/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "OpenSSL",
                imageResId = R.drawable.ic_xyn_android,
                url = "URL: https://github.com/openssl/openssl/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "FFmpeg",
                imageResId = R.drawable.ic_xyn_android,
                url = "URL: https://github.com/FFmpeg/FFmpeg/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "busybox",
                imageResId = R.drawable.ic_xyn_linux,
                url = "URL: https://github.com/mirror/busybox/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Magisk",
                imageResId = R.drawable.ic_xyn_magisk,
                url = "URL: https://github.com/topjohnwu/Magisk/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "KernelSU",
                imageResId = R.drawable.ic_xyn_kernelsu,
                url = "URL: https://github.com/tiann/KernelSU/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "LSPosed",
                imageResId = R.drawable.ic_xyn_lsp,
                url = "URL: https://github.com/LSPosed/LSPosed/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Zygisk Next",
                imageResId = R.drawable.ic_xyn_modules,
                url = "URL: https://github.com/Dr-TSNG/ZygiskNext/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "HyperCeiler",
                imageResId = R.drawable.ic_xyn_hyperceiler,
                url = "URL: https://github.com/ReChronoRain/HyperCeiler/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Cemiuiler",
                imageResId = R.drawable.ic_xyn_hyperceiler,
                url = "URL: https://github.com/Sevtinge/Cemiuiler/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Shizuku",
                imageResId = R.drawable.ic_xyn_shizuku,
                url = "URL: https://github.com/RikkaApps/Shizuku/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Shizuku API",
                imageResId = R.drawable.ic_xyn_shizuku,
                url = "URL: https://github.com/RikkaApps/Shizuku-API/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Dhizuku",
                imageResId = R.drawable.ic_xyn_android,
                url = "URL: https://github.com/iamr0s/Dhizuku/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Dhizuku API",
                imageResId = R.drawable.ic_xyn_android,
                url = "URL: https://github.com/iamr0s/Dhizuku-API/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Xposed",
                imageResId = R.drawable.ic_xyn_xposed,
                url = "URL: https://github.com/topics/xposed/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "LSPatch",
                imageResId = R.drawable.ic_xyn_lsp,
                url = "URL: https://github.com/LSPosed/LSPatch/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Npatch",
                imageResId = R.drawable.ic_xyn_lsp,
                url = "URL: https://github.com/HSSkyBoy/NPatch/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "LibChecker",
                imageResId = R.drawable.ic_xyn_libchecker,
                url = "URL: https://github.com/LibChecker/LibChecker/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "JsHook",
                imageResId = R.drawable.ic_xyn_jshook,
                url = "URL: https://github.com/jsHookApp/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "AppManager",
                imageResId = R.drawable.ic_xyn_appmanager,
                url = "URL: https://github.com/MuntashirAkon/AppManager/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Termux",
                imageResId = R.drawable.ic_xyn_termux,
                url = "URL: https://github.com/termux/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Termux NDK",
                imageResId = R.drawable.ic_xyn_termux,
                url = "URL: https://github.com/lzhiyong/termux-ndk/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Riru",
                imageResId = R.drawable.ic_xyn_modules,
                url = "URL: https://github.com/RikkaApps/Riru/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Play Integrity Fix",
                imageResId = R.drawable.ic_xyn_modules,
                url = "URL: https://github.com/chiteroman/PlayIntegrityFix/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "WAuxiliary",
                imageResId = R.drawable.ic_xyn_wa,
                url = "URL: https://github.com/HdShare/WAuxiliary_Public/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "QAuxiliary",
                imageResId = R.drawable.ic_xyn_qa,
                url = "URL: https://github.com/cinit/QAuxiliary/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Kitsune Mask",
                imageResId = R.drawable.ic_xyn_kitsune_mask,
                url = "URL: https://github.com/HuskyDG/magisk-files/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Key Attestation",
                imageResId = R.drawable.ic_xyn_key,
                url = "URL: https://github.com/vvb2060/KeyAttestation/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Arthas",
                imageResId = R.drawable.ic_xyn_java,
                url = "URL: https://github.com/alibaba/arthas/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Limbo",
                imageResId = R.drawable.ic_xyn_limbo,
                url = "URL: https://github.com/limboemu/limbo/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Winlator",
                imageResId = R.drawable.ic_xyn_winlator,
                url = "URL: https://github.com/brunodev85/winlator/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "WSABuilds",
                imageResId = R.drawable.ic_xyn_wsa,
                url = "URL: https://github.com/MustardChef/WSABuilds/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Linux Kernel",
                imageResId = R.drawable.ic_xyn_linux,
                url = "URL: https://github.com/torvalds/linux/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Reqable",
                imageResId = R.drawable.ic_xyn_reqable,
                url = "URL: https://github.com/reqable/reqable-app/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "XiaoMi Kernel",
                imageResId = R.drawable.ic_xyn_mi,
                url = "URL: https://github.com/MiCode/Xiaomi_Kernel_OpenSource/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "WSA pacman installer",
                imageResId = R.drawable.ic_xyn_wsa,
                url = "URL: https://github.com/alesimula/wsa_pacman/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Android",
                imageResId = R.drawable.ic_xyn_android,
                url = "URL: https://github.com/Android/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Google",
                imageResId = R.drawable.ic_xyn_google,
                url = "URL: https://github.com/google/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "AndroidIDE",
                imageResId = R.drawable.ic_xyn_androidide,
                url = "URL: https://github.com/AndroidIDEOfficial/AndroidIDE/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "AndroidIDE NDK",
                imageResId = R.drawable.ic_xyn_androidide,
                url = "URL: https://github.com/MrIkso/AndroidIDE-NDK/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "AndroidIDE NDK",
                imageResId = R.drawable.ic_xyn_androidide,
                url = "URL: https://github.com/HomuHomu833/android-ndk-custom/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "AndroidIDE SDK",
                imageResId = R.drawable.ic_xyn_androidide,
                url = "URL: https://github.com/AndroidIDEOfficial/androidide-tools/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "AndroidIDE SDK",
                imageResId = R.drawable.ic_xyn_androidide,
                url = "URL: https://github.com/AndroidIDEOfficial/platform-tools/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "TTS Server",
                imageResId = R.drawable.ic_xyn_tts,
                url = "URL: https://github.com/jing332/tts-server-android/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Xposed Api",
                imageResId = R.drawable.ic_xyn_xposed,
                url = "URL: https://github.com/libxposed/api/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "TrickyStore",
                imageResId = R.drawable.ic_xyn_modules,
                url = "URL: https://github.com/5ec1cff/TrickyStore/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "DisableFlagSecureDetector",
                imageResId = R.drawable.ic_xyn_modules,
                url = "URL: https://github.com/5ec1cff/DisableFlagSecureDetector/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "aShellYou",
                imageResId = R.drawable.ic_xyn_ashell,
                url = "URL: https://github.com/DP-Hridayan/aShellYou/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Telegram Desktop",
                imageResId = R.drawable.ic_xyn_telegram,
                url = "URL: https://github.com/telegramdesktop/tdesktop/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Termux NDK",
                imageResId = R.drawable.ic_xyn_termux,
                url = "URL: https://github.com/jzinferno2/termux-ndk/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Linux Sudo",
                imageResId = R.drawable.ic_xyn_linux,
                url = "URL: https://github.com/sudo-project/sudo/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Jetpack Compose Material Design 3",
                imageResId = R.drawable.ic_xyn_android,
                url = "URL: https://github.com/maxkeppeler/sheets-compose-dialogs/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "ApkSignatureKillerEx",
                imageResId = R.drawable.ic_xyn_android,
                url = "URL: https://github.com/L-JINBIN/ApkSignatureKillerEx/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "LibGDX",
                imageResId = R.drawable.ic_xyn_game,
                url = "URL: https://github.com/libgdx/libgdx/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "Windows Super god mode",
                imageResId = R.drawable.ic_xyn_dat,
                url = "URL: https://github.com/ThioJoe/Windows-Super-God-Mode/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "LLVM",
                imageResId = R.drawable.ic_xyn_android,
                url = "URL: https://github.com/llvm/llvm-project/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "abseil-cpp",
                imageResId = R.drawable.ic_xyn_android,
                url = "URL: https://github.com/abseil/abseil-cpp/",
                iconResId = R.drawable.ic_xyn_github
            ),
            ArtItem(
                title = "protobuf",
                imageResId = R.drawable.ic_xyn_android,
                url = "URL: https://github.com/protocolbuffers/protobuf/",
                iconResId = R.drawable.ic_xyn_github
            )
        )
        _artItems.value = items
    }
}