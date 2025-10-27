package com.saif.posttest4pmob

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.saif.posttest4pmob.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private lateinit var adapter: CitizenAdapter

    private lateinit var dao: CitizenDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java,
            "citizen.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        dao = db.citizenDao()


        val statusArr = resources.getStringArray(R.array.status_pernikahan)
        b.spStatus.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            statusArr
        )

        adapter = CitizenAdapter(mutableListOf())
        b.rvCitizen.layoutManager = LinearLayoutManager(this)
        b.rvCitizen.adapter = adapter


        lifecycleScope.launch {
            dao.getAll().collectLatest { list ->
                adapter.clearAll()
                list.forEach { e ->
                    adapter.add(
                        Citizen(
                            nama = e.nama,
                            nik = e.nik,
                            kabupaten = e.kabupaten,
                            kecamatan = e.kecamatan,
                            desa = e.desa,
                            rt = e.rt,
                            rw = e.rw,
                            gender = e.gender,
                            statusPernikahan = e.statusPernikahan
                        )
                    )
                }
            }
        }

        // --- Simpan ke DB ---
        b.btnSimpan.setOnClickListener {
            val data = collectForm() ?: return@setOnClickListener

            lifecycleScope.launch {
                dao.insert(
                    CitizenEntity(
                        nama = data.nama,
                        nik = data.nik,
                        kabupaten = data.kabupaten,
                        kecamatan = data.kecamatan,
                        desa = data.desa,
                        rt = data.rt,
                        rw = data.rw,
                        gender = data.gender,
                        statusPernikahan = data.statusPernikahan
                    )
                )
            }
            Toast.makeText(this, "Data tersimpan ke database", Toast.LENGTH_SHORT).show()
            resetForm(clearList = false)
        }
        b.btnReset.setOnClickListener {
            lifecycleScope.launch {
                dao.clear()
            }
            resetForm(clearList = true)
            Toast.makeText(this, "Semua data dihapus", Toast.LENGTH_SHORT).show()
        }
    }

    private fun collectForm(): Citizen? {
        val nama = b.etNama.text?.toString()?.trim().orEmpty()
        val nikStr = b.etNik.text?.toString()?.trim().orEmpty()
        val kab = b.etKabupaten.text?.toString()?.trim().orEmpty()
        val kec = b.etKecamatan.text?.toString()?.trim().orEmpty()
        val desa = b.etDesa.text?.toString()?.trim().orEmpty()
        val rtStr = b.etRT.text?.toString()?.trim().orEmpty()
        val rwStr = b.etRW.text?.toString()?.trim().orEmpty()

        val gender = when (b.rgGender.checkedRadioButtonId) {
            b.rbLaki.id -> "Laki-Laki"
            b.rbPerempuan.id -> "Perempuan"
            else -> ""
        }
        val status = b.spStatus.selectedItem?.toString() ?: ""

        // --- Validasi field teks wajib ---
        if (nama.isEmpty()) { b.etNama.error = "Wajib diisi"; return null }
        if (kab.isEmpty()) { b.etKabupaten.error = "Wajib diisi"; return null }
        if (kec.isEmpty()) { b.etKecamatan.error = "Wajib diisi"; return null }
        if (desa.isEmpty()) { b.etDesa.error = "Wajib diisi"; return null }
        if (gender.isEmpty()) { toast("Pilih jenis kelamin"); return null }

        // --- Validasi NIK 16 digit angka ---
        if (nikStr.length != 16 || nikStr.any { !it.isDigit() }) {
            b.etNik.error = "NIK harus 16 digit angka"
            return null
        }
        val nik = nikStr.toLong()   // aman karena sudah valid 16 digit

        // --- Validasi & parse RT/RW ---
        val rt = rtStr.toIntOrNull()
        val rw = rwStr.toIntOrNull()
        if (rt == null) { b.etRT.error = "RT harus angka"; return null }
        if (rw == null) { b.etRW.error = "RW harus angka"; return null }

        return Citizen(
            nama = nama,
            nik = nik,
            kabupaten = kab,
            kecamatan = kec,
            desa = desa,
            rt = rt,
            rw = rw,
            gender = gender,
            statusPernikahan = status
        )
    }

    private fun resetForm(clearList: Boolean) {
        b.etNama.setText("")
        b.etNik.setText("")
        b.etKabupaten.setText("")
        b.etKecamatan.setText("")
        b.etDesa.setText("")
        b.etRT.setText("")
        b.etRW.setText("")
        b.rgGender.clearCheck()
        b.spStatus.setSelection(0)

        if (clearList) adapter.clearAll()
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
