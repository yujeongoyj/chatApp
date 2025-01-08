package com.project.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

// 데이터와 메시지 화면 연결 어댑터
// 받을 viewHolder 클래스가 받는쪽, 보낸쪽 두 개이기 때문에 RecyclerView.ViewHolder를 통해 어떤 ViewHolder든 받을 수 있도록
class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 사용자uId에 따라 어떤 ViewHolder를 사용할지 정하기 위한 변수 2개
    private val receive = 1 // 받는 타입
    private val send = 2 // 보내는 타입

    // 화면 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // 밑의 getItemViewType 에서 리턴된 값이 onCreateViewHolder의 ViewType에 들어 있음
        return if(viewType == 1) { // 받는 화면
            val view : View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            ReceiveViewHolder(view)
        } else { // 보내는 화면
            val view : View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            SendViewHolder(view)
        }
    }

    // 데이터 연결
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position] // 현재 메시지
        // 보내는 데이터
        // ViewHolder 객체가 SendViewHolder의 타입의 객체이면 보내는 데이터
        if(holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            viewHolder.sendMessage.text = currentMessage.message
        } else { // 받는 데이터
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    // 어떤 ViewHolder를 사용할지
    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position] // 메시지 값
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sendId)) {
            send
        } else {
            receive
        }
    }


    // RecyclerView의 ViewHolder는 RecyclerView의 각 아이템을 관리하는 클래스
    // 죽, viewHolder는 RecyclerView의 각 항목에 대한 뷰를 유지하고 재활용하는 역할
    // itemView는 보통 onCreateViewHolder에서 LayoutInflater를 통해 인플레이트된 뷰 객체

    // 받는 쪽 viewHolder
    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMessage: TextView = itemView.findViewById(R.id.send_message_text)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.receive_message_text)
    }
}