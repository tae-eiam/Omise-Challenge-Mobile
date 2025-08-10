package com.example.omisechallenge.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.omisechallenge.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val navController = (supportFragmentManager.findFragmentById(R.id.content_fragment) as NavHostFragment).navController
        navController.setGraph(R.navigation.emarket_nav_graph)
    }
}