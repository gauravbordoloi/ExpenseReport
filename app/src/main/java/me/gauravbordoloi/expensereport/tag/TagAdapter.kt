package me.gauravbordoloi.expensereport.tag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_tag.view.*
import me.gauravbordoloi.expensereport.R

class TagAdapter : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    private val tags = arrayListOf<String>()

    fun setTags(tags: List<String>?) {
        if (tags.isNullOrEmpty()) {
            this.tags.clear()
            notifyDataSetChanged()
            return
        }
        this.tags.clear()
        this.tags.addAll(tags)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = tags.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textTag.text = tags[position]
        holder.itemView.setOnClickListener {
            //it.findNavController() TODO
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textTag = view.textTag

    }

}