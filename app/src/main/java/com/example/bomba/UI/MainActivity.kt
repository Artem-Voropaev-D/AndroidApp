package com.example.bomba.UI

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.bomba.R
import com.example.bomba.UI.FragmentOne
import com.example.bomba.UI.FragmentTwo
import com.example.bomba.UI.data

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data.rectangles_draw.add(Rectangl(1, 1, 11, "1", 1))
        data.rectangles_draw.add(Rectangl(10, 4, 14, "2", 8))
        data.rectangles_draw.add(Rectangl(2, 2, 6, "3", 7))
        data.rectangles_draw.add(Rectangl(4, 3, 2, "4", 10))
        data.rectangles_draw.add(Rectangl(1, 6, 6, "5", 6))
        data.rectangles_draw.add(Rectangl(3, 7, 22, "6", 4))
        data.rectangles_draw.add(Rectangl(5, 1, 13, "7", 8))
        data.rectangles_draw.add(Rectangl(5, 4, 7, "8", 1))
        data.rectangles_draw.add(Rectangl(1, 3, 4, "9", 5))
        data.rectangles_draw.add(Rectangl(2, 1, 5, "a", 2))
        data.rectangles_draw.add(Rectangl(1, 1, 7, "b", 2))
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //начальная страница 1-й фрагмент
        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        var fragment: Fragment = FragmentOne()
        ft.replace(R.id.fr_place, fragment)
        ft.commit()

    }

    fun Change(view: View) {
        var fragment: Fragment? = null

        when(view.id){
            R.id.button4 ->{
                fragment = FragmentOne()
            }

            R.id.button2 -> {
                fragment = FragmentOne()
            }
            R.id.button3 ->{
                fragment = FragmentTwo()
            }
        }

        Log.i("MyTag", "Пользователь ввел длину: ${data.length_r}")

        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        if ( fragment != null){
            ft.replace(R.id.fr_place, fragment)
        }
        ft.commit()

    }
}