 package com.example.android_shimmer_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.android_shimmer_example.adapter.MainAdapter
import com.example.android_shimmer_example.data.User
import kotlinx.android.synthetic.main.activity_main.*

 class MainActivity : AppCompatActivity() {

    lateinit var adapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpUI();
         Thread.sleep(1000)
        setupAPICall()
    }


     private fun setUpUI() {
         recyclerView.layoutManager = LinearLayoutManager(this)
         adapter = MainAdapter(arrayListOf())
         recyclerView.addItemDecoration(
             DividerItemDecoration(
                 recyclerView.context,
                 (recyclerView.layoutManager as LinearLayoutManager).orientation
         )
         )
            recyclerView.adapter = adapter
     }

     private fun setupAPICall() {

         AndroidNetworking.initialize(applicationContext)
         AndroidNetworking.get("https://5e510330f2c0d300147c034c.mockapi.io/users")
             .build()
             .getAsObjectList(User::class.java, object : ParsedRequestListener<List<User>>{
                 override fun onResponse(response: List<User>) {
                     shimmerFrameLayout.startShimmerAnimation()
                     shimmerFrameLayout.visibility = View.GONE
                     recyclerView.visibility = View.VISIBLE
                     adapter.addUser(response)
                     adapter.notifyDataSetChanged()
                 }

                 override fun onError(anError: ANError?) {
                     shimmerFrameLayout.visibility = View.GONE
                     Toast.makeText(this@MainActivity, "Something went wrong ", Toast.LENGTH_LONG).show()

                 }

             })
     }
     override fun onResume() {
         super.onResume()
         shimmerFrameLayout.startShimmerAnimation()
     }

     override fun onPause() {
         shimmerFrameLayout.stopShimmerAnimation()
         super.onPause()
     }

 }