package com.example.bomba

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import java.util.ArrayList
import java.util.Random

class Data(
    var length_r : Int = 0,
    var width_r : Int = 0,
    var height_r : Int = 0,
    var weight_r : Int = 0,
    val rectangles_draw: MutableList<Rectangl> = mutableListOf(), //тут имя и размеры предметов
    val rectanglesF: MutableList<Rectangl> = mutableListOf()
){
    companion object{
        val data = Data()
    }
}


class Rectangl(var width: Int, var length: Int, var height: Int, var name: String, var weight: Int) {
    var startX: Int = 0 // Начальная позиция X для отрисовки
    var color: Int = Color.BLACK


    fun rotate() {
        val rot = width
        width = length
        length = rot
    }
}

internal object RectanglePacking {
    fun packRectangles(rectangles: MutableList<Rectangl>, stripWidth: Int, striplength: Int, weight: Int, maxHeight: Int, ImageView2 : ImageView): Int {
        var maxWeight = weight
        var proverka = true

        val tcer: MutableList<String> = ArrayList()

        val t = rectangles.size
        for (i in 0 until t) {
            val recta = Rectangl(rectangles[i].width, rectangles[i].length, rectangles[i].height , rectangles[i].name, rectangles[i].weight)
            recta.rotate()
            recta.color = rectangles[i].color
            rectangles.add(recta)
        }

        // Сортировка прямоугольников по убыванию высоты
        rectangles.sortWith(Comparator { r1, r2 -> r2.length - r1.length })

        var currentlength: Int

        val levels: MutableList<Level> = ArrayList()

        for (rect in rectangles) {
            if (maxWeight - rect.weight >= 0 && rect.height <= maxHeight){
                proverka = true

                for (str in tcer) { //Гуляю по массиву и смотрю совпадение в именах ректанглов, чтобы исключить случаи повтора ректанглов
                    if (str == rect.name) {
                        proverka = false
                    }
                }
                if (!proverka) continue

                currentlength = striplength
                for (level in levels) {
                    currentlength -= level.length
                }

                var packed = false

                // Попытка упаковать на существующий уровень
                for (level in levels) {
                    if (level.canPlaceFloor(rect)) {
                        tcer.add(rect.name)
                        level.placeFloor(rect)
                        maxWeight -= rect.weight
                        packed = true
                        break
                    }
                    if (level.canPlaceCeiling(rect)) {
                        tcer.add(rect.name)
                        level.placeCeiling(rect)
                        maxWeight -= rect.weight
                        packed = true
                        break
                    }
                }
                // Если не удалось упаковать, создаем новый уровень
                if (!packed) {
                    if (rect.length <= currentlength && rect.height <= maxHeight) {
                        tcer.add(rect.name)
                        val newLevel = Level(stripWidth, rect.length, currentlength)
                        newLevel.placeFloor(rect)
                        maxWeight -= rect.weight
                        levels.add(newLevel)
                    }
                }
            }
        }


        // Вычисляем итоговую высоту упаковки
        var totallength = 0
        for (level in levels) {
            totallength += level.length
        }

        //        for (Level level : levels.reversed()) {
//            for (int i = 0; i < level.ceilingRectangles.size(); i++){
//                System.out.print(level.ceilingRectangles.get(i).name);
//            }
//        }
//        for (Rectangl r: rectangles){
//            System.out.println(STR."\{r.name} \{r.width} \{r.length}");
//        }
        var sc = 0;
        for (level in levels) {
            level.drawAllRectangles(sc, ImageView2)
            sc += level.length
        }
        return totallength
    }

}

internal class Level(maxWidth: Int, rectlength: Int, currentlength: Int) {
    var maxWidth: Int = 0 //Максимальная ширина уровня
    var floorWidth: Int = 0 //Суммарная ширина фигур на полу
    var ceilingWidth: Int = 0 //Суммарная ширина фигур на потолке
    var length: Int = 0 //Длина уровня(Зависит от самого длинного прямоугольника на уровне)
    var ceilinglength: Int = 0 //Суммарная длина фигур на потолке

    var currentWidth: Int = 0
    var floorRectangles: MutableList<Rectangl> = ArrayList() //Массив прямоугольников на полу
    var ceilingRectangles: MutableList<Rectangl> = ArrayList() //Массив прямоугольников на потолке


    init {
        if (rectlength <= currentlength) {
            this.length = rectlength
            this.maxWidth = maxWidth
        }
    }

    fun canPlaceCeiling(rect: Rectangl): Boolean {
        currentWidth = maxWidth
        for (rectangl in floorRectangles) {
            if (rectangl.length > this.length - rect.length) {
                currentWidth -= rectangl.width
            } else break
        }
        return ceilingWidth + rect.width <= currentWidth
    }

    fun canPlaceFloor(rect: Rectangl): Boolean {
        if (rect.length <= length) {
            return floorWidth + rect.width <= maxWidth
        }
        return false
    }

    fun placeFloor(rect: Rectangl) {
        if (canPlaceFloor(rect)) {
            rect.startX = floorWidth
            floorWidth += rect.width
            floorRectangles.add(rect)
        }
    }

    fun placeCeiling(rect: Rectangl) {
        if (canPlaceCeiling(rect)) {
            rect.startX = maxWidth - ceilingWidth - 1
            ceilingWidth += rect.width //Суммарная ширина фигур на потолке для первого уровня, спускаясь ниже нужно занулить.
            ceilinglength =
                kotlin.math.max(ceilinglength.toDouble(), rect.length.toDouble()).toInt() //для первой фигуры братка
            ceilingRectangles.add(rect)
        }
    }


    fun drawAllRectangles(sc: Int, ImageView2: ImageView){
        val daTa = Data.data
        var bitmap = Bitmap.createBitmap(700, 1000, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)

        var StartY = 900f - sc.toFloat()*850f/ daTa.length_r//откуда рисуется первый прямоугольник левый нижний угол по вертикали
        var StartX = 50f //откуда рисуется первый прямоугольник левый нижний угол по горизонтали
        for (rect in floorRectangles) {

            daTa.rectanglesF.add(rect)

            val paint = Paint()
            val random = Random()
            paint.color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 5f
            //НУЖНО СДЕЛАТЬ ОТНОСИТЕЛЬНО РАЗМЕРОВ ПРОСТРАНСТВА КОТОРЫЕ ОН ВВЕЛ
            val top = rect.length.toFloat()*850f/daTa.length_r//длина
            val right = rect.width.toFloat()*600f/daTa.width_r//ширина
            //примерно 700 1000
            canvas.drawRect(50f, 50f, 650f, 900f, paint)//само пространство
            paint.style = Paint.Style.FILL
            rect.color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
            paint.color = rect.color
            canvas.drawRect(StartX, StartY - top, StartX + right, StartY, paint)
            StartX += 600f*rect.width/ daTa.width_r
        }

        StartY = 900f - sc.toFloat()*850f/ daTa.length_r//откуда рисуется первый прямоугольник левый нижний угол по вертикали
        StartX = 650f //откуда рисуется первый прямоугольник левый нижний угол по горизонтали

        for (rect in ceilingRectangles) {
            daTa.rectanglesF.add(rect)


            val paint = Paint()
            val random = Random()
            paint.color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 5f
            //НУЖНО СДЕЛАТЬ ОТНОСИТЕЛЬНО РАЗМЕРОВ ПРОСТРАНСТВА КОТОРЫЕ ОН ВВЕЛ
            val top = rect.length.toFloat()*850f/daTa.length_r//длина
            val right = rect.width.toFloat()*600f/daTa.width_r//ширина
            //примерно 700 1000
            canvas.drawRect(50f, 50f, 650f, 900f, paint)//само пространство
            paint.style = Paint.Style.FILL
            rect.color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
            paint.color = rect.color
            canvas.drawRect(StartX - right, StartY - this.length*850f/ daTa.length_r, StartX , StartY - this.length*850f/ daTa.length_r + top, paint)
            StartX -= 600f*rect.width/ daTa.width_r

        }
        ImageView2.setImageBitmap(bitmap)

    }
}