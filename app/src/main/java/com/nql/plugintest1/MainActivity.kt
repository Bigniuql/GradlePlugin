package com.nql.plugintest1

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_click.setOnClickListener {
            Log.d("======", "you click the textView")
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "Start!", Toast.LENGTH_LONG).show()
    }
}