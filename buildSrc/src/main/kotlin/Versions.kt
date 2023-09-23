object Versions {
    fun getVersionCode() = 44
    fun getVersionName() = "$majorVersion.$minorVersion.$patchVersion"
    private const val majorVersion = 3
    private const val minorVersion = 4
    private const val patchVersion = 0
}