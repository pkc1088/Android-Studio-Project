package com.example.week7

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.week7.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.timer
import kotlin.properties.Delegates


class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore                         //파이어베이스.파이어스토어 설정
    var oSysMainLoop = 0                                //타임어 쓰레드 0=시작 조차 않음. 1=실행중, 2= 실행 후 종료했음

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //xloc 값이 변경되면 xloc 값을 읽어서 읽어서 textview에 그리는 부분
        val adocRef = db.collection("user").document("ZcBLwPBKT8eugmpq6CXA")

        adocRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
            if (snapshot != null && snapshot.exists()) {
                binding.textView.text = snapshot.data!!["xloc"].toString()
            }
        })
        //타임어로 xloc 값을 일정 주기로 계속 바꾸는 부분
        if (oSysMainLoop == 0) {     //타임어 쓰레드를 실행한 적이 없다면
            oSysMainLoop = 1         //타임어 쓰레드 실행
            timer(period = 1500, initialDelay = 1000)     //0.5초(500) 주기마다 호출,처음 시작시 딜레이 1초
            {
                if (oSysMainLoop != 1) {
                    cancel()
                }
                val axloc = hashMapOf("xloc" to (0..100).random())
                db.collection("user").document("ZcBLwPBKT8eugmpq6CXA").set(axloc) //YBwvoo1dQbgflP8uomWp
                    .addOnSuccessListener {
                        Toast.makeText(this@MainActivity, "데이터 추가 성공", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@MainActivity, "데이터 추가 실패", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}