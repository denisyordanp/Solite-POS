object Versions {
    // for now to define if the app should force update is to set the version code of multiple 5
    fun getVersionCode() = 47
    fun getVersionName() = "$majorVersion.$minorVersion.$patchVersion"
    private const val majorVersion = 3
    private const val minorVersion = 5
    private const val patchVersion = 0
}