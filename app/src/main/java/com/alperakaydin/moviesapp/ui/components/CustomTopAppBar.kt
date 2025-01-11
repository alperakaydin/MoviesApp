package com.alperakaydin.moviesapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alperakaydin.moviesapp.R
import com.alperakaydin.moviesapp.ui.theme.CustomTextColor
import com.alperakaydin.moviesapp.ui.theme.zentokyo


@Composable
fun CustomTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 28.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "MoviesApp",
            fontSize = 34.sp,
            fontFamily = zentokyo,
            fontWeight = FontWeight.Bold,
            color = CustomTextColor
        )
        Image(
            painter = painterResource(id = R.drawable.ic_user), // Profil resmi
            contentDescription = "User Profile",
            modifier = Modifier
                .size(35.dp)
                .padding(end = 6.dp)
            //.clip(CircleShape)
        )
    }
}
