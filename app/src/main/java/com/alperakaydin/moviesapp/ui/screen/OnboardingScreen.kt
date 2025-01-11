package com.alperakaydin.moviesapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


import com.alperakaydin.moviesapp.R
import com.alperakaydin.moviesapp.ui.theme.CustomBackground
import com.alperakaydin.moviesapp.ui.theme.CustomBackground2
import com.alperakaydin.moviesapp.ui.theme.CustomButtonColor
import com.alperakaydin.moviesapp.ui.theme.CustomTextColor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    val pages = listOf(
        Pair("Welcome to the Movie App", R.raw.welcomelottie),
        Pair("Secure Payment!", R.raw.card),
        Pair("Fast Delivery!", R.raw.fastlottie)
    )
    val pagerState = rememberPagerState(
        initialPage = 0, initialPageOffsetFraction = 0f, pageCount = { pages.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomBackground),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.height(16.dp)) // Üst boşluk
        // Animasyon ve Metin
        HorizontalPager(state = pagerState) { page ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                val animationSpec = LottieCompositionSpec.RawRes(pages[page].second)
                val composition by rememberLottieComposition(spec = animationSpec)
                val progress by animateLottieCompositionAsState(composition)

                // Animasyon
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(250.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Metin
                Text(
                    text = pages[page].first,
                    color = CustomTextColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
// Noktalar
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            repeat(pages.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 18.dp else 12.dp)
                        .padding(4.dp)
                        .background(
                            color = if (pagerState.currentPage == index) Color.White
                            else Color.Gray.copy(alpha = 0.8f),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )
            }
        }
        // Navigation Button
        Button(
            onClick = {
                if (pagerState.currentPage == pages.size - 1) {
                    onComplete()
                } else {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomButtonColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
            , shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (pagerState.currentPage == pages.size - 1) "Finish" else "Next")
        }

        Spacer(modifier = Modifier.height(16.dp)) // Alt boşluk

    }


}
