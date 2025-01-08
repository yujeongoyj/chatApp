package com.project.chat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.chat.databinding.ActivityChatBinding

import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {
    // 상대 정보를 담을 변수
    private lateinit var receiverName: String
    private lateinit var receiverUid: String

    // 바인딩 객체
    private lateinit var binding: ActivityChatBinding
    lateinit var mAuth: FirebaseAuth // 인증 객체
    lateinit var mDbRef: DatabaseReference // 데이터 베이스 객체

    private lateinit var receiverRoom: String // 받는 대화방
    private lateinit var senderRoom: String // 보낸 대화방

    // 메시지 담을 변수
    private lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 초기화
        messageList = ArrayList()
        val messageAdapter: MessageAdapter = MessageAdapter(this, messageList)

        // RecyclerView
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = messageAdapter

        // 넘어온 데이터 intent로 꺼내와 변수에 담기
        receiverName =
            intent.getStringExtra("name").toString() // UserAdapter로부터 "name" 키로 전달된 데이터 받기
        receiverUid = intent.getStringExtra("uId").toString()  // "uId" 키로 전달된 데이터 받기

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        // 접속자 uId
        val senderUid = mAuth.currentUser?.uid
        // 보낸이 방
        senderRoom = receiverUid + senderUid
        // 받는이 방
        receiverRoom = senderUid + receiverUid

        // 액션바에 상대방 이름 보여주기
        supportActionBar?.title = receiverName

        // 메시지 버튼 이벤트 (보낸쪽 받는 쪽 모두에 데이터 저장)
        binding.sendBtn.setOnClickListener {
            // 받은 메시지 클래스 형태로 받아서
            val message = binding.messageEdit.text.toString()
            val messageObject = Message(message, senderUid)
            // DB에 저장
            // "chats" 이름의 DB를 생성하고 하위에 senderRoom 그 하위에 messages 공간 생성 후 푸시(아까 저장한 메시지 객체 값으로)
            mDbRef.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    // 저장 성공하면 받는 이의 공간(receiverRoom) 에도 저장
                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                }
            // 메시지 보낸 후 입력 부분 초기화
            binding.messageEdit.setText("")
        }


        // messageAdapter로 연결한 메시지 데이터 가져오기
        mDbRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {

                // DB의 chat > senderRoom > message 내용에 변경이 생겼을 데이터를 가져오는 함수
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!) // !!는 널값이 아님을 보장하는 연산자
                    }
                    // 적용
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

}