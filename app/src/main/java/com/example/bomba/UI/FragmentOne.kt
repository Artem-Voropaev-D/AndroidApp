package com.example.bomba.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.bomba.Logic.Data
import com.example.bomba.R

val data: Data = Data.data
class FragmentOne : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button1 = view.findViewById<Button>(R.id.button1)


        button1.setOnClickListener {
            val length1 = view.findViewById<EditText>(R.id.length) //берем введенную длину
            val width1 = view.findViewById<EditText>(R.id.width)
            val height1 = view.findViewById<EditText>(R.id.height)
            val weight1 = view.findViewById<EditText>(R.id.weight)

            if(length1.text.toString().isNotEmpty() &&
                width1.text.toString().isNotEmpty() &&
                height1.text.toString().isNotEmpty() &&
                weight1.text.toString().isNotEmpty() ){

                data.length_r = length1.text.toString().toInt()
                data.width_r = width1.text.toString().toInt()
                data.height_r = height1.text.toString().toInt()
                data.weight_r = weight1.text.toString().toInt()

            }

            // Создайте новый фрагмент
            val fragmentThree = FragmentTwo()

            // Получите FragmentManager
            val fragmentManager = parentFragmentManager

            // Начните транзакцию фрагмента
            val fragmentTransaction = fragmentManager.beginTransaction()

            // Замените текущий фрагмент на новый
            fragmentTransaction.replace(R.id.fr_place, fragmentThree)
            // Добавьте транзакцию в backstack (опционально)
            fragmentTransaction.addToBackStack(null)
            // Выполните транзакцию
            fragmentTransaction.commit()
        }
    }


}