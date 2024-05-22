package com.example.bomba


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView

class FragmentThree : Fragment() {

    private lateinit var imageView2: ImageView
    private lateinit var rectSpinner1: Spinner
    private lateinit var rectAdapterF: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_three, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rectSpinner1 = view.findViewById(R.id.rectSpinnerF)
        imageView2 = view.findViewById(R.id.imageView2)


        val button4 = view.findViewById<Button>(R.id.button4)
        button4.setOnClickListener {
            data.rectanglesF.clear()//обнуляем массив с предметами
            imageView2.clearColorFilter()
            data.rectangles_draw.clear()

            val fragmentThree = FragmentOne()

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

        RectanglePacking.packRectangles(
            data.rectangles_draw,
            data.width_r,
            data.length_r,
            data.weight_r,
            data.height_r,
            imageView2
        )
        updateRectSpinnerF(data.rectanglesF.size)

    }

    private fun updateRectSpinnerF(rectangleCount: Int) {
        val rectDescriptions = mutableListOf<String>()
        for (i in 1..rectangleCount) {
            val rect = data.rectanglesF[i - 1]
            rectDescriptions.add("${rect.name} : (${rect.length}, ${rect.width}, ${rect.height}, ${rect.weight})")
        }

        rectAdapterF = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, rectDescriptions) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).setTextColor(data.rectanglesF[position].color)
                return view
            }
        }
        rectSpinner1.adapter = rectAdapterF
        rectSpinner1.setSelection(rectangleCount - 1) // Выбираем последний добавленный Rect в списке
    }
}