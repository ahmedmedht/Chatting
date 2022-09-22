package com.example.chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDref:DatabaseReference

    var receiverRoom: String?=null
    var senderRoom:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        mDref=FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom= senderUid + receiverUid

        supportActionBar?.title =name
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)

        chatRecy.layoutManager =LinearLayoutManager(this)
        chatRecy.adapter=messageAdapter

        //logic for addong data to recyclerView
            mDref.child("chats").child(senderRoom!!).child("messages")
                .addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        messageList.clear()

                        for (postSnapshot in snapshot.children){
                            val message = postSnapshot.getValue(Message::class.java)
                            messageList.add(message!!)
                        }
                        messageAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
        //adding the message to database
        sentbtn.setOnClickListener {
            val message= messageBox.text.toString()
            val messageObjects =Message(message,senderUid)

            mDref.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObjects).addOnSuccessListener {
                    mDref.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObjects)
                }
            messageBox.setText("")
        }



    }
}