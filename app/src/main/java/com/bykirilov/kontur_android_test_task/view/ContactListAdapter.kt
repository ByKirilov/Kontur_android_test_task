package com.bykirilov.kontur_android_test_task.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bykirilov.kontur_android_test_task.R
import com.bykirilov.kontur_android_test_task.model.database.Contact

class ContactListAdapter(val itemClickListener: OnItemClickListener) : ListAdapter<Contact, ContactListAdapter.ContactViewHolder>(ContactComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, itemClickListener)
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactNameItemView: TextView = itemView.findViewById(R.id.contact_name)
        private val contactPhoneItemView: TextView = itemView.findViewById(R.id.contact_phone)
        private val contactHeightItemView: TextView = itemView.findViewById(R.id.contact_height)

        fun bind(contact: Contact?,
                 itemClickListener: OnItemClickListener) {
            contactNameItemView.text = contact?.name
            contactPhoneItemView.text = contact?.phone
            contactHeightItemView.text = contact?.height.toString()

            contact?.let { contact ->
                itemView.setOnClickListener{ itemClickListener.onItemClicked(contact) }
            }
        }

        companion object {
            fun create(parent: ViewGroup): ContactViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return ContactViewHolder(view)
            }
        }
    }

    class ContactComparator : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.phone == newItem.phone
                    && oldItem.height == newItem.height
        }

    }
}