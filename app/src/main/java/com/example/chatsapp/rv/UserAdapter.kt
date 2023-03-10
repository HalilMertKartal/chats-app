package com.example.chatsapp.rv

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatsapp.R
import com.example.chatsapp.data.User
import com.example.chatsapp.ui.ChatActivity


class UserAdapter(val context:Context, val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_listitem_layout, parent, false)
        return UserViewHolder(view)
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.username.text = currentUser.username
        
        // Add an onclicklistener to the holder to be trigger when user presses one of the names on the list
        holder.itemView.setOnClickListener {
            // Jump to a chat activity by passing some info
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("username", currentUser.username)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }
    override fun getItemCount():Int{ return userList.size}

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById<TextView>(R.id.textView)
    }
}