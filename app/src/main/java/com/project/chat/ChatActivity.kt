package com.project.chat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ChatActivity : AppCompatActivity() {
    // 대화 상대 정보를 담을 변수
    private lateinit var receiverName: String
    private lateinit var receiverUid: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 넘어온 데이터 intent로 꺼내와 변수에 담기
        receiverName = intent.getStringExtra("name").toString() // UserAdapter로부터 "name" 키로 전달된 데이터 받기
        receiverUid = intent.getStringExtra("uId").toString()  // "uId" 키로 전달된 데이터 받기

        // 액션바에 상대방 이름 보여주기
        supportActionBar?.title = receiverName

        // 채팅방으로 연결하는 코드는 UserAdapter에
    }

}