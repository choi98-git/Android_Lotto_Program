package com.example.lotto_program

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private  val clearButton: Button by lazy{
        findViewById<Button>(R.id.clearButton)
    }

    private  val addButton: Button by lazy{
        findViewById<Button>(R.id.addButton)
    }

    private val runButton: Button by lazy {
        findViewById<Button>(R.id.runButton)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private  val numTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.firstNumberText),
            findViewById(R.id.secondNumberText),
            findViewById(R.id.thirdNumberText),
            findViewById(R.id.fourthNumberText),
            findViewById(R.id.fifthNumberText),
            findViewById(R.id.sixthNumberText)
        )
    }


    private var didRun = false // 예외처리 상태 변수

    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NumberPicker의 1~45까지 범위 지정
        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()

    }
    // 자동 생성 버튼
    private fun initRunButton(){
        runButton.setOnClickListener {
            val list = getRandomNumber()
            didRun = true
            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true

                setNumberBackground(number, textView)
            }

        }
    }

    // 번호 추가 버튼
    private fun initAddButton(){
        addButton.setOnClickListener {
            // 이미 모든 수를 선택했을 때
            if(didRun){
                Toast.makeText(this,"초기화 후에 시도해주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 선택 가능한 수의 개수를 초과했을 때
            if (pickNumberSet.size >= 5){
                Toast.makeText(this,"번호는 5개까지만 선택 가능합니다!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 중복된 수를 선택했을 때
            if(pickNumberSet.contains(numberPicker.value)){
                Toast.makeText(this,"이미 선택한 번호입니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val textView = numTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            setNumberBackground(numberPicker.value, textView)


            pickNumberSet.add(numberPicker.value)
        }
    }
    
    // 숫자 범위별 Background Color 지정
    private fun setNumberBackground(number:Int, textView: TextView){
        when(number){
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 10..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 20..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 30..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }

    // 최기화 버튼
   private fun initClearButton(){
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            // 리스트를 하나씩 꺼내와서 visivible를 false로 설정
            numTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }

    private fun getRandomNumber(): List<Int>{
        // numberList에 Int형인 1~45수를 담음
        val numberList = mutableListOf<Int>().apply {
            for(i in 1..45){
                // 자동 번호 생성하기 전 이미 선택한 수가 있다면
                if(pickNumberSet.contains(i)){
                    continue
                }
                this.add(i)
            }
        }
        // numberList에 담긴 수를 shuffle함수를 통해 순서를 섞어줌
        numberList.shuffle()

        // subList함수를 통해 numberList의 인덱스 0~5까지의 6개의 수를 newList에 담음
        // 이미 선택된 수를 담는 pickNumberSet리스트와  나머지 수를 자동 생성 하여 구성된 리스트를 더하여
        // newList로 생성
       val newList = pickNumberSet.toList() + numberList.subList(0,6 - pickNumberSet.size)


        // newList에 담긴 수를 정렬하여 return
        return newList.sorted()
    }

}