package com.saif.posttest4pmob

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saif.posttest4pmob.databinding.ItemCitizenBinding

class CitizenAdapter(
    private val items: MutableList<Citizen>
) : RecyclerView.Adapter<CitizenAdapter.VH>() {

    inner class VH(val b: ItemCitizenBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        return VH(ItemCitizenBinding.inflate(inf, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        holder.b.tvTitle.text = "${position + 1}. ${c.nama} (${c.gender}) - ${c.statusPernikahan}"
        holder.b.tvNik.text   = "NIK: ${c.nik}"
        holder.b.tvAlamat.text = "Alamat: RT ${c.rt}/RW ${c.rw}, ${c.desa}, ${c.kecamatan}, ${c.kabupaten}"
    }

    override fun getItemCount() = items.size

    fun add(c: Citizen) {
        items.add(c)
        notifyItemInserted(items.size - 1)
    }

    fun clearAll() {
        items.clear()
        notifyDataSetChanged()
    }
}