object Versions {
    fun getVersionCode() = "$majorVersion$minorVersion$patchVersion".toInt()
    fun getVersionName() = "$majorVersion.$minorVersion.$patchVersion"
    private const val majorVersion = 3
    private const val minorVersion = 3
    private const val patchVersion = 7

    // This is the latest version code before we do an automatically generate version code
    private const val latestVersionCode = 42
}