package com.prince.bmitracker

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.prince.bmitracker.ui.theme.BMITrackerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        installSplashScreen()

        setContent {
            BMITrackerTheme {
                BMITracker()
            }
        }
    }
}
@Preview    
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMITracker(){
    val backgroundColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.tertiary
    val textGrey = Color(0xFF666666)

    var heightSelected by rememberSaveable { mutableStateOf(170f) }
    var weightSelected by rememberSaveable { mutableStateOf(50f) }
    var age by rememberSaveable { mutableStateOf(20) }

    //Fonts
    val alata = FontFamily(
        Font(R.font.alata)
    )
    val aoboshi = FontFamily(
        Font(R.font.aoboshi_one)
    )
    val poppins = FontFamily(
        Font(R.font.poppins)
    )



    val maleSelected = rememberSaveable { mutableStateOf(true) }
    val femaleSelected = rememberSaveable { mutableStateOf(false) }


    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    ConstraintLayout( modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor) ){
        val (head, male, female, heightBox, weightBox, ageBox, bmi, more, profile, bottom_bar) = createRefs()
        val barrier = createBottomBarrier(male, female)

        Text(
            text= "BMI Calculator",
            fontSize = 42.sp,
            fontFamily = poppins,
            fontWeight = FontWeight.Black,
            color = primaryColor,
            modifier= Modifier
                .constrainAs(head) {
                    top.linkTo(parent.top)

                }
                .padding(start = 17.dp, top = 48.dp, bottom = 7.dp)
        )
        //Male
        Box(
            modifier = Modifier
                .constrainAs(male) {
                    start.linkTo(parent.start)
                    end.linkTo(female.start)
                    top.linkTo(head.bottom)
                    bottom.linkTo(heightBox.top)
                }
                .padding(end = 15.dp)
                .fillMaxWidth(0.35f)
                .aspectRatio(1f)
                .border(
                    BorderStroke(
                        width = if (maleSelected.value) 5.dp else 2.dp,
                        color = if (maleSelected.value) Color(0xFF1E90FF) else Color(0xFFADD8E6)
                    ),
                    shape = RoundedCornerShape(25.dp)
                )
                .background(
                    color = if (maleSelected.value) Color(0x3B818181) else Color.Transparent,
                    shape = RoundedCornerShape(25.dp)
                )
                .clickable(
                    onClick = {
                        maleSelected.value = true
                        femaleSelected.value = false
                    },
                    indication = rememberRipple(
                        color = Color.Gray,
                        radius = 60.dp,
                        bounded = false
                    ),
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            Image(modifier= Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(17.dp) , painter = painterResource(id = R.drawable.male), contentDescription = "male")
        }
        //Female box
        Box(
            modifier = Modifier
                .constrainAs(female) {
                    start.linkTo(male.end)
                    end.linkTo(parent.end)
                    top.linkTo(head.bottom)
                    bottom.linkTo(heightBox.top)

                }
                .padding(start = 15.dp)
                .fillMaxWidth(0.35f)
                .aspectRatio(1f)
                .border(
                    BorderStroke(
                        width = if (femaleSelected.value) 5.dp else 2.dp,
                        color = if (femaleSelected.value) Color(0xFFDC143C) else Color(0xFFFF89B1)
                    ),
                    shape = RoundedCornerShape(25.dp)
                )
                .background(
                    color = if (femaleSelected.value) Color(0x3B818181) else Color.Transparent,
                    shape = RoundedCornerShape(25.dp)
                )
                .clickable(
                    onClick = {
                        femaleSelected.value = true
                        maleSelected.value = false

                    },
                    indication = rememberRipple(
                        color = Color.Gray,
                        radius = 60.dp,
                        bounded = false
                    ),
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            Image(modifier= Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(17.dp) , painter = painterResource(id = R.drawable.female), contentDescription = "male")
        }


        //Input for the Height
        Column(
            modifier = Modifier
                .constrainAs(heightBox) {
                    top.linkTo(barrier)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(weightBox.top)
                }
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(
                    color = Color(0x0F818181), // Set the background color
                    shape = RoundedCornerShape(16.dp) // Set rounded corners for background
                )
                .border(
                    BorderStroke(2.dp, Color(0xFF858585)), // Set border with color and thickness
                    shape = RoundedCornerShape(19.dp) // Set rounded corners for the border
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "${heightSelected.toInt()}",
                fontSize = 32.sp,
                color = primaryColor,
                fontFamily = aoboshi,
                modifier = Modifier.padding(bottom = 6.dp, top = 12.dp)
            )
            Text(
                text = "Height (in cm)",
                fontFamily = poppins,
                fontSize = 20.sp,
                color = textGrey,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            Slider(
                value = heightSelected,
                onValueChange = { newHeight -> heightSelected = newHeight },
                valueRange = 120f..200f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF1E90FF), // Custom thumb color
                    activeTrackColor = Color(0xFFADD8E6), // Active track color
                    inactiveTrackColor = Color(0xFFFFACC8) // Inactive track color
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (i in 120..200 step 10) {
                    Column(
                        modifier = Modifier.width(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(10.dp)
                                .background(primaryColor),

                        )
                        Text(
                            text = i.toString(),
                            fontSize = 10.sp,
                            color = primaryColor,
                            modifier = Modifier.align(Alignment.CenterHorizontally) // Center text below the box
                        )
                    }
                }
            }
        }
        //For weight box
        Column(
            modifier = Modifier
                .constrainAs(weightBox) {
                    top.linkTo(heightBox.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(ageBox.start)
                    bottom.linkTo(bottom_bar.top)
                }
                .background(
                    color = Color(0x0F818181),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    BorderStroke(2.dp, Color(0xFF858585)),
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth(0.45f)
                .fillMaxHeight(0.28f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$weightSelected",
                color = primaryColor,
                fontFamily = alata,
                fontSize = 29.sp,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(
                text = "Weight (in kg)",
                fontFamily = poppins,
                fontSize = 17.sp,
                color = textGrey,
                modifier = Modifier.padding(bottom = 6.dp)
            )


            val scrollState = rememberScrollState()

            LaunchedEffect(Unit) {
                val defaultIndex = 75
                scrollState.scrollTo(defaultIndex * 50) // Adjust the scroll position based on item height
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (l_weight in 20..150) {
                    Text(
                        text = "$l_weight",
                        fontSize = 25.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                weightSelected = l_weight.toFloat()
                            },
                        color = if (l_weight.toFloat() == weightSelected) Color(0xFF1E90FF) else secondaryColor,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        //Age box
        Column(
            modifier = Modifier
                .constrainAs(ageBox) {
                    top.linkTo(heightBox.bottom)
                    start.linkTo(weightBox.end)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottom_bar.top)
                }
                .background(
                    color = Color(0x0F818181),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    BorderStroke(2.dp, Color(0xFF858585)),
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxWidth(0.45f)
                .fillMaxHeight(0.28f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$age",
                color = Color(0xFFFF518C),
                fontSize = 35.sp,
                fontFamily = alata,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Button(
                onClick = { age++ },
                modifier = Modifier
                    .padding(horizontal = 35.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "+", fontSize = 25.sp)
            }
            Text(
                text = "age",
                fontFamily = poppins,
                fontSize = 24.sp,
                color = textGrey,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Button(
                onClick = { if (age > 0) age-- },
                modifier = Modifier
                    .padding(horizontal = 35.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "-", fontSize = 25.sp)
            }

        }
        Button(onClick = {
            showBottomSheet = true
            calculateBMI(weightSelected, heightSelected, age, maleSelected)
        },modifier= Modifier

            .constrainAs(bmi) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            .height(105.dp)
            .width(65.dp)
            .padding(bottom = 42.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors =ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2))
        ) {
            Text(text = "BMI", fontFamily = aoboshi, color=Color.White, fontSize = 30.sp,textAlign = TextAlign.Center, modifier= Modifier.padding(0.dp))
        }


        //Bottom Bar
        Image(
            painter = painterResource(id = R.drawable.bottom_bar),
            contentDescription = null,
            modifier= Modifier
                .constrainAs(bottom_bar) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
                .padding(top = 13.dp)
        )
        var showDialog by rememberSaveable { mutableStateOf(false) }
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .constrainAs(more) {
                    start.linkTo(parent.start)
                    end.linkTo(bmi.start)
                    top.linkTo(bottom_bar.top)
                    bottom.linkTo(bottom_bar.bottom)
                }
                .clickable {
                    showDialog = true // Show the dialog on click
                },
            tint= textColor
        )

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("How BMI Calculator works?") },
                text = { Text("Inputs: The user provides their weight, height, age, and gender.\n\n" +
                        "BMI Formula: The formula used is:\n" +
                        "\uD835\uDC35\uD835\uDC40\uD835\uDC3C = Weight (kg)/(Height (m))2\n" +
                        " \n" +
                        "BMI Classification: Based on the calculated BMI value, the app categorizes the person into:\n" +
                        "Underweight (BMI < 18.5)\n" +
                        "Normal weight (BMI 18.5–24.9)\n" +
                        "Overweight (BMI 25.0–29.9)\n" +
                        "Obesity (BMI >= 30)\n" +
                        "\n" +
                        "Age and Gender Advice: The app provides additional analysis based on the user’s age group and offers gender-specific health advice.") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Got it !")
                    }
                }
            )
        }


        val activity = LocalContext.current as? Activity
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Exit",
            modifier = Modifier
                .size(48.dp)

                .constrainAs(profile) {
                    start.linkTo(bmi.end)
                    end.linkTo(parent.end)
                    top.linkTo(bottom_bar.top)
                    bottom.linkTo(bottom_bar.bottom)
                }
                .clickable {
                    activity?.finish()
                },
            tint= textColor
        )

    }


    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Your BMI is ", fontFamily = poppins, fontSize = 20.sp, color = secondaryColor, modifier= Modifier.padding(bottom = 10.dp))
                Text(text = bmiResult, fontFamily = aoboshi, fontSize = 47.sp, color = Color(0xFF1877F2), modifier= Modifier.padding(bottom = 7.dp))
                Text(text = bmiCategoryResult, fontFamily = alata, fontSize = 30.sp, color = Color(0xFF1565C0), modifier = Modifier.padding(bottom = 5.dp))
                Divider(modifier= Modifier.padding(vertical = 27.dp))
                Text(text = "$genderSpecificAdviceResult\n$ageAnalysisResult", fontFamily = poppins, fontSize = 20.sp, color = secondaryColor)
                Button(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    },
                    modifier= Modifier.padding(vertical = 25.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }


}


var bmiResult: String = ""
var bmiCategoryResult: String = ""
var ageAnalysisResult: String = ""
var genderSpecificAdviceResult: String = ""

fun calculateBMI(weightSelected: Float, heightSelected: Float, age: Int, maleSelected: MutableState<Boolean>) {

    val bmi = weightSelected / ((heightSelected / 100) * (heightSelected / 100))
    bmiResult = "%.2f".format(bmi)


    // Determine BMI category based on WHO standards
    bmiCategoryResult = when {
        bmi < 18.5 -> "Underweight"
        bmi in 18.5..24.9 -> "Normal weight"
        bmi in 25.0..29.9 -> "Overweight"
        else -> "Obesity"
    }

    // Analyze based on age groups (general health insights)
    ageAnalysisResult = when {
        age < 18 -> "BMI values can vary greatly in younger individuals. Consult a doctor for child-specific analysis."
        age in 18..24 -> "Young adults can maintain a lower BMI for optimal health."
        age in 25..34 -> "Maintaining a healthy BMI in your mid-20s to early 30s is important for long-term health."
        age in 35..44 -> "It's common for BMI to slightly increase as you age, but staying within normal ranges is still crucial."
        age >= 45 -> "Older adults should focus on keeping a balanced BMI to reduce the risk of chronic conditions."
        else -> "Consult healthcare professionals for personalized advice."
    }

    // Provide gender-specific advice based on BMI category
    genderSpecificAdviceResult = if (maleSelected.value) {
        when (bmiCategoryResult) {
            "Underweight" -> "Males with low BMI might have nutritional deficiencies. Consider consulting a healthcare provider."
            "Overweight", "Obesity" -> "Men with high BMI may have a higher risk of heart disease and diabetes."
            else -> "Keep maintaining a healthy lifestyle!"
        }
    } else {
        when (bmiCategoryResult) {
            "Underweight" -> "Females with low BMI may face risks like osteoporosis or fertility issues."
            "Overweight", "Obesity" -> "Women with higher BMI are at an increased risk for conditions like PCOS or heart disease."
            else -> "Great job maintaining a healthy weight!"
        }
    }
}






