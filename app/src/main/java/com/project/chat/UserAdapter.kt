package com.project.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// RecyclerView를 사용하여 사용자 리스트를 표시하는 클래스
// 사용자 데이터를 화면에 보여주기 위한 RecyclerView와 데이터를 연결하는 역할
// UserAdapter는 RecyclerView.Adapter를 상속받고 있으며,
// UserViewHolder라는 내부 클래스를 사용하여 각 항목을 표시
// context(Activity, Fragment 등)을 전달받고 이 컨텍스트는 LayoutInflater를 사용할 때 필요
// userList는 사용자 정보를 담고있는 ArrayList객체로, 화면에 표시될 리스트의 내용을 제공
class UserAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

        /*
        화면 설정
         */

    // onCreateViewHolder는 user layout을 설정하는 함수
        // layout을 view에 넣고 view를 UserViewHolder에 넣는다
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // LayoutInflater 객체는 xml 파일을 실제 뷰 객체로 변환하는 역할
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    /*
    데이터 설정
     */

    // 데이터를 연결해주는 함수
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // 데이터 설정
        // userList에 있는 데이터를 순서대로 currentUser에 넣기
        val currentUser = userList[position]
        // currentUser에 있는 이름을 텍스트뷰에 넣기
        holder.nameText.text = currentUser.name

        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            // 넘길 데이터
            // intent에 name과 uId를 담아서 StartActivity에 전달
            // -> ChatActivity에서도 intent를 통해 데이터를 받을 수 있음
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uId", currentUser.uId)
            context.startActivity(intent)
        }
    }

    // 실제 리스트의 갯수를 가져오는 함수
    override fun getItemCount(): Int {
        return userList.size
    }


    // RecyclerView의 ViewHolder를 상속받은 클래스
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.name_text)
    }


}