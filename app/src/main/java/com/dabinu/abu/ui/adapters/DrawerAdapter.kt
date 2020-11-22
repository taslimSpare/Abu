package com.dabinu.abu.ui.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dabinu.abu.R
import com.dabinu.abu.models.DrawerItem

class DrawerAdapter(
    private val context: Context,
    private val drawerItems: List<DrawerItem>,
    private val clickListener: (DrawerItem) -> Unit):
        RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder>() {


    inner class DrawerViewHolder(itemView: View):
            RecyclerView.ViewHolder(itemView) {

        private val root = itemView.findViewById<RelativeLayout>(R.id.drawerItemRoot)
        private val icon = itemView.findViewById<ImageView>(R.id.ivDrawerIcon)
        private val title = itemView.findViewById<TextView>(R.id.tvDrawerItemTitle)
        private val drawerSelected = itemView.findViewById<View>(R.id.viewDrawerSelected)

        fun bind(item: DrawerItem) {
            if (item.icon != 0)
                icon.setImageResource(item.icon)

            val itemColor = if (item.selected) {
                drawerSelected.visibility = View.VISIBLE
                root.setBackgroundColor(ContextCompat.getColor(context, R.color.drawer_selected))
                R.color.app_green
            }else {
                drawerSelected.visibility = View.INVISIBLE
                val out = TypedValue()
                context.theme.resolveAttribute(android.R.attr.selectableItemBackground, out, true)
                root.setBackgroundResource(out.resourceId)
                R.color.black
            }

            icon.setColorFilter(ContextCompat.getColor(context, R.color.app_green))
            title.setTextColor(ContextCompat.getColor(context, itemColor))
            title.text = item.title

            root.setOnClickListener {
                drawerItems.forEach { next -> next.selected = false }
                item.selected = true

                notifyDataSetChanged()
                clickListener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DrawerViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_drawer_item, parent, false))

    override fun getItemCount() = drawerItems.size
    override fun onBindViewHolder(holder: DrawerViewHolder, position: Int) {
        holder.bind(drawerItems[position])
    }
}