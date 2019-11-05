package feri.com.observationtool.data

import java.io.Serializable

class Catatan(
    val judul_catatan: String?,
    val tipe_kegiatan: Int?,
    val deskripsi: String?,
    val time: Long?,
    val file_uri: String?
) : Serializable