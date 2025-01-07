package com.project.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.InternalTextApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.chat.databinding.ActivityLoginInBinding

class LoginActivity : AppCompatActivity() {
    // activity_log-in.xml 바탕으로 자동으로 만들어진 바인딩 객체
    lateinit var binding: ActivityLoginInBinding

    // 파이어베이스 인증 서비스 객체 가져오기
    lateinit var mAuth: FirebaseAuth

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
        // 인증 초기화
        mAuth = Firebase.auth
        // 로그인 버튼 이벤트
        binding.loginBtn.setOnClickListener {
            // 폼에 입력된 정보를 담을
            val email = binding.emailEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

            login(email, password)

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

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent: Intent = Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    finish() // finish() 함수로 액티비티를 닫아줌
                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                    Log.d("Login", "Error: ${task.exception}")

                }
            }
    }
}