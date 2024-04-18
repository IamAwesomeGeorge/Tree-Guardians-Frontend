package com.example.tg.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tg.R
import com.example.tg.models.TreeModel

class TreeListAdapter(private val trees: List<TreeModel>) : RecyclerView.Adapter<TreeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tree, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tree = trees[position]
        holder.bind(tree)
    }

    override fun getItemCount(): Int = trees.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val treeSpeciesTextView: TextView = itemView.findViewById(R.id.treeSpeciesTextView)
        private val treeDetailsTextView: TextView = itemView.findViewById(R.id.treeDetailsTextView)

        fun bind(tree: TreeModel) {
            // Ensure species is bold
            val speciesBold = "<b>${tree.species}</b>"

            // Add formatted tree species and icon to tile
            treeSpeciesTextView.text = HtmlCompat.fromHtml(
                itemView.context.getString(
                    R.string.tree_species_format,
                    speciesBold
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            // Add formatted tree details to tile
            treeDetailsTextView.text = itemView.context.getString(
                R.string.tree_details_format,
                tree.height.toString(),
                tree.circumference.toString(),
                tree.healthStatus
            )
        }
    }
}
