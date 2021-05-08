package com.jadhavrupesh.firestorepractice

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.jadhavrupesh.firestorepractice.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    var db = FirebaseFirestore.getInstance()

    var isOnline = false

    lateinit var binding: ActivityMainBinding

    lateinit var adapter: FirebaseAdapter

    private var itemList = arrayListOf<UserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        recyclerViewSetup()
        FetchLiveFirestoreData()
        addDataToFireStore()

    }

    private fun FetchLiveFirestoreData() {   // this use for fetching list of data
        FirebaseFirestore.getInstance().collection("chatUser")
            .addSnapshotListener { value, _ ->
                if (value != null) {
                    if (itemList.size == value.documents.size) {
                        value.documentChanges.forEach { updatedValue ->
                            itemList.removeAt(updatedValue.newIndex)
                            itemList.add(
                                updatedValue.newIndex,
                                updatedValue.document.toObject(UserModel::class.java)
                            )
                        }
                    } else {
                        itemList.clear()
                        value.toObjects(UserModel::class.java).let { itemList.addAll(it) }
                    }
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun addDataToFireStore() {
        binding.saveBTN.setOnClickListener {
            binding.progress.visibility = View.VISIBLE

            val numberString: String = binding.textInputNumber.getEditText()?.text.toString()
            val number: Int = numberString.toInt()

            for (i in 0 until number) {
                val refId = db.collection("chatUser").document().id
                val rnds = (0..10).random()
                val name = getSaltString(5)
                val sname = getSaltString(7)
                isOnline = (rnds % 2) == 0
                val userModel = UserModel(name, sname, isOnline)
                db.collection("chatUser").document().set(userModel).addOnCompleteListener {
                    Toast.makeText(this, "Data Added Successfully...", Toast.LENGTH_SHORT)
                        .show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Something Goes Wrong...", Toast.LENGTH_SHORT).show()
                }
            }
            binding.progress.visibility = View.GONE
        }

    }

    private fun recyclerViewSetup() {
        adapter = FirebaseAdapter()
        binding.recyclerView.adapter = adapter
        adapter.items = itemList
        adapter.notifyDataSetChanged()
    }


    protected fun getSaltString(number: Int): String {
        val SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        val salt = StringBuilder()
        val rnd = Random()
        while (salt.length < number) { // length of the random string.
            val index = (rnd.nextFloat() * SALTCHARS.length).toInt()
            salt.append(SALTCHARS[index])
        }
        return salt.toString()
    }
}