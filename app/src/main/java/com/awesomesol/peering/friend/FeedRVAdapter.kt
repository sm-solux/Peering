package com.awesomesol.peering.friend

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.awesomesol.peering.R
import com.awesomesol.peering.activity.MainActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FeedRVAdapter(val items : ArrayList<FeedModel>) : RecyclerView.Adapter<FeedRVAdapter.Viewholder>() {

    val storage= FirebaseStorage.getInstance()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.feed_rv_item, parent, false)
        return Viewholder(v)
    }

    // Item 클릭을 위한 추가
    interface ItemClick{
        fun onClick(view: View, position: Int)
    }
    var itemClick: ItemClick? = null

    // onCreateViewHolder에서 가져와서 view에 실제 데이터 연결
    override fun onBindViewHolder(holder: FeedRVAdapter.Viewholder, position: Int) {
        // Item클릭을 위한 추가
        if(itemClick != null){
            holder?.itemView?.setOnClickListener { v ->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(items[position])
    }

    // item의 총 갯수
    override fun getItemCount(): Int {
        return items.size
    }
    inner class Viewholder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var cid:String=""
        var uid:String=""
        var nickname : String = ""
        var mainImg : String = ""
        var profileImg : String = ""
        var content : String = ""

        // feed_rv_item의 item의 값들을 하나하나 넣어주는 코드
        fun bindItems(item : FeedModel){
            cid= item.cid
            uid=item.uid
            content=item.content
            profileImg=item.profileImg
            mainImg=item.mainImg


            val tv_FeedRVItem_nickname = itemView.findViewById<TextView>(R.id.tv_FeedRVItem_nickname)
            tv_FeedRVItem_nickname.text = item.nickname
            val tv_FeedRVItem_content = itemView.findViewById<TextView>(R.id.tv_FeedRVItem_content)
            tv_FeedRVItem_content.text = content

            val iv_FeedRVItem_profileImg = itemView.findViewById<ImageView>(R.id.iv_FeedRVItem_profileImg)
            Glide.with(itemView)
                    .load(profileImg)
                    .circleCrop()
                    .into(iv_FeedRVItem_profileImg)
            val iv_FeedRVItem_mainImg=itemView.findViewById<ImageView>(R.id.iv_FeedRVItem_mainImg)

             storage.reference.child(uid).child(cid).child(mainImg).downloadUrl
                    .addOnSuccessListener { imageUri->
                        Glide.with(itemView)
                                .load(imageUri)
                                .into(iv_FeedRVItem_mainImg);
                    }
                     .addOnFailureListener {
                         Glide.with(itemView)
                                 .load(R.drawable.feed_main_img)
                                 .into(iv_FeedRVItem_mainImg);
                     }
        }
    }


}