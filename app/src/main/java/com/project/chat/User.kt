package com.project.chat

// 사용자 정보를 담을 데이터 클래스
data class User(
    var name: String,
    var email: String,
    var uId: String
) {
    constructor(): this("", "", "")
}
