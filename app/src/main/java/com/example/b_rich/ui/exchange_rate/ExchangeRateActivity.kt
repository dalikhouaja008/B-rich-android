import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.b_rich.R
import com.example.b_rich.ui.theme.PREF_FILE



@Composable
fun ExchangeRate() {

    val context = LocalContext.current
    val mSharedPreferences = remember { context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE) }
    //background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3D5AFE), Color(0xFFB39DDB))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //txt titree
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3D5AFE),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Hello there, sign in to continue",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            //logo
            val logo: Painter = painterResource(id = R.drawable.logo)
            Image(
                painter = logo,
                contentDescription = "Logo",
                modifier = Modifier.size(250.dp)
            )

            ClickableText(
                text = AnnotatedString("log out"),
                onClick = {
                    val editor=mSharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                    (context as? Activity)?.finishAffinity()
                },
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF3D5AFE),
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))


        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    ExchangeRate()
}


