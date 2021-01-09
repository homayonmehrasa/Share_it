package ir.kurd.shareit.utiles

object Constants {
    const val DEVELOPED_BY = "Arvin"
    const val REQUEST_STORAGE_WRITE_PERMISSION = 666
    val photoExtensions: Array<String> get() = arrayOf(".jpg", ".png", ".jpeg", ".bmp", ".webp", ".heic", ".heif")
    val videoExtensions: Array<String> get() = arrayOf(".mp4", ".mkv", ".webm", ".avi", ".3gp", ".mov", ".m4v", ".3gpp")
    val audioExtensions: Array<String> get() = arrayOf(".mp3", ".wav", ".wma", ".ogg", ".m4a", ".opus", ".flac", ".aac")
    val rawExtensions: Array<String> get() = arrayOf(".dng", ".orf", ".nef", ".arw", ".rw2", ".cr2", ".cr3")
    const val NOMEDIA = ".nomedia"
    const val MD5 = "MD5"

}