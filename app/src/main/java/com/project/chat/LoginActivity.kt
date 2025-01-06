package com.project.chat

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.project.chat.databinding.ActivityLoginInBinding

class LoginActivity : AppCompatActivity() {
    // activity_log-in.xml 바탕으로 자동으로 만들어진 바인딩 객체
    lateinit var binding: ActivityLoginInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 회원 가입 버튼 이벤트
        binding.signUpBtn.setOnClickListener {
            // Intent로 Activity 간의 이동
            // Intent안의 객체로는 Context와 이동할 클래스 입력
            // ::class.java 는 코틀린 클래스를 자바 클래스 객체로 변환하는 역할
            val intent: Intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}