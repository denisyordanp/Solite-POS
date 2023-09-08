object Versions {
    fun getVersionCode() = 43
    fun getVersionName() = "$majorVersion.$minorVersion.$patchVersion"
    private const val majorVersion = 3
    private const val minorVersion = 3
    private const val patchVersion = 8
}