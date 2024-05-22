package com.example.bomba

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast


class FragmentTwo : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var etLeft: EditText
    private lateinit var etTop: EditText
    private lateinit var etRight: EditText
    private lateinit var etBottom: EditText
    private lateinit var name: EditText
    private lateinit var rectSpinner: Spinner
    private lateinit var rectAdapter: ArrayAdapter<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false)
    }
    // Внутри FragmentOne.kt

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonDrawAll = view.findViewById<Button>(R.id.btnDrawAll)
        imageView = view.findViewById<ImageView>(R.id.imageView)
        etLeft = view.findViewById<EditText>(R.id.etLeft)
        etTop = view.findViewById<EditText>(R.id.etTop)
        etRight = view.findViewById<EditText>(R.id.etRight)
        etBottom = view.findViewById<EditText>(R.id.etBottom)
        rectSpinner = view.findViewById<Spinner>(R.id.rectSpinner)
        name = view.findViewById<EditText>(R.id.name)
        updateRectSpinner(data.rectangles_draw.size)

        buttonDrawAll.setOnClickListener {
            // Создайте новый фрагмент
            val fragmentThree = FragmentThree()

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

        val addRect = view.findViewById<Button>(R.id.btnDrawRectangle)
        addRect.setOnClickListener{
            addRectangle()
            etLeft.text.clear()
            etBottom.text.clear()
            etRight.text.clear()
            etTop.text.clear()
            name.text.clear()
        }
    }

    private fun addRectangle() {
        val weightItem = etBottom.text.toString().toInt()
        val lengthItem = etRight.text.toString().toInt()
        val widthItem = etTop.text.toString().toInt()
        val heightItem = etLeft.text.toString().toInt()
        val Name = name.text.toString()

        data.rectangles_draw.add(Rectangl(widthItem, lengthItem, heightItem, Name, weightItem))

        updateRectSpinner(data.rectangles_draw.size)

        Toast.makeText(requireContext(), "Rectangle added", Toast.LENGTH_SHORT).show()
    }

    private fun updateRectSpinner(rectangleCount: Int) {
        val rectDescriptions = mutableListOf<String>()
        for (i in 1..rectangleCount) {
            val rect = data.rectangles_draw[i - 1]
            rectDescriptions.add("${rect.name}: (${rect.length}, ${rect.width}, ${rect.height}, ${rect.weight})")
        }

        rectAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rectDescriptions)
        rectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rectSpinner.adapter = rectAdapter
        rectSpinner.setSelection(rectangleCount - 1) // Выбираем последний добавленный Rect в списке
    }


}