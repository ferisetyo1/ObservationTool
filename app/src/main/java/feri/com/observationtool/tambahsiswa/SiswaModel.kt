package feri.com.observationtool.tambahsiswa

import java.io.Serializable

class SiswaModel : Serializable {

    var no_absen: Int? = 0
    var nama_siswa: String? = ""

    constructor(no_absen: Int?, nama_siswa: String?) {
        this.no_absen = no_absen
        this.nama_siswa = nama_siswa
    }

    override fun toString(): String {
        return "SiswaModel(no_absen=$no_absen, nama_siswa=$nama_siswa)"
    }


}