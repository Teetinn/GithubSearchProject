package id.ac.umn.githubsearchproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import id.ac.umn.githubsearchproject.data.model.ResponseUser
import id.ac.umn.githubsearchproject.databinding.ItemUserBinding

class UserAdapter(private val data:MutableList<ResponseUser.Item> = mutableListOf(),
private val listener:(ResponseUser.Item) -> Unit): RecyclerView.Adapter<UserAdapter.UserViewHolder>(){
    fun setData(data:MutableList<ResponseUser.Item>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class UserViewHolder(private val v: ItemUserBinding) : RecyclerView.ViewHolder(v.root){
        fun bind(item: ResponseUser.Item){
            v.ivUser.load(item.avatar_url)
            v.tvUsername.text = item.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder = UserViewHolder(ItemUserBinding.inflate(
        LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size
}