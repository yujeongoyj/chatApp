package com.project.chat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.project.chat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 바인딩 객체
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: UserAdapter

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    // 데이터를 담을 UserList
    private lateinit var userList:ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // 객체들 초기화
        mAuth = Firebase.auth
        mDbRef = Firebase.database.reference
        userList = ArrayList()
        // 컨텍스트와 userList를 넘겨주고
        // 이 데이터는 UserAdapter 클래스에서 받게 됨
        adapter = UserAdapter(this, userList)

        // binding으로 가져온 RecyclerView 설정
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        // RecyclerView의 dapter는 UserAdapter로 설정
        binding.userRecyclerView.adapter = adapter

        // Realtime Database에서 사용자 정보 가져오기
        // 이 DB 객체를 통해서 user에 있는 데이터를 가져오겠다는 뜻
        mDbRef.child("user").addValueEventListener(object :ValueEventListener {
            // onDataChange 함수는 데이터가 변경되면 실행
            override fun onDataChange(snapshot: DataSnapshot) {
                // onDataChange 함수 매개변수인 snapshot에 데이터베이스가 들어있고 그 내용들을 posSnapshot에 넣음
                for(postSnapshot in snapshot.children) {
                    // 하지만 채팅앱이므로 로그인했을때 본인 제외한 유저가 보여야함
                    val currentUser = postSnapshot.getValue(User::class.java)
                    // 저 currentUser 안에 실제 사용자 정보 넣기
                    // mAuth(인증 객체)로 현재 로그인한 uid를 알 수 있음
                    if(mAuth.currentUser?.uid != currentUser?.uId) { // 내 id와 등록된 사용자 id가 다를 때만
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged() // 얘를 통해 데이터가 실제 화면에 적용됨
            }

            // 실패 시 실행
            override fun onCancelled(error: DatabaseError) {

            }

        })



    }
}