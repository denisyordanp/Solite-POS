object Versions {
    fun getVersionCode() = 47
    fun getVersionName() = "$majorVersion.$minorVersion.$patchVersion"
    private const val majorVersion = 3
    private const val minorVersion = 5
    private const val patchVersion = 0
}