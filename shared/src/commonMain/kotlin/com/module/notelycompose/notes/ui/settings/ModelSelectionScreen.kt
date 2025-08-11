package com.module.notelycompose.notes.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.detail.AndroidNoteTopBar
import com.module.notelycompose.notes.ui.detail.IOSNoteTopBar
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.platform.getPlatform
import kotlinx.coroutines.delay

private const val HIDE_TIME_ELAPSE = 1500L

data class ModelOption(
    val title: String,
    val description: String,
    val size: String
)

@Composable
fun ModelSelectionScreen(
    navigateBack: () -> Unit
) {
    val modelOptions = listOf(
        ModelOption(
            title = "Standard model (multilingual)",
            description = "Faster performance and smaller file size\nSupports multiple languages except Hindi",
            size = "139 MB"
        ),
        ModelOption(
            title = "Optimized Model (Multilingual + Hindi)",
            description = "Highest accuracy available\nSupports Hindi and all other languages\nLarger file size, slower performance",
            size = "488 MB"
        )
    )

    var selectedModel by remember { mutableIntStateOf(0) } // Standard model selected by default
    var isProgressVisible by remember { mutableStateOf(false) }
    var isCheckMarkVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.bodyBackgroundColor)
    ) {
        if (getPlatform().isAndroid) {
            AndroidNoteTopBar(
                title = "",
                onNavigateBack = navigateBack
            )
        } else {
            IOSNoteTopBar(
                onNavigateBack = navigateBack
            )
        }

        if (isProgressVisible) {
            LinearProgressIndicator(
                modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(),
                strokeCap = StrokeCap.Round
            )
        } else {
            Spacer(
                modifier = Modifier.padding(vertical = 14.dp).fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            // Title
            Text(
                text = "Model Selection",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = LocalCustomColors.current.bodyContentColor,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Subtitle
            Text(
                text = "AI Model",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = LocalCustomColors.current.bodyContentColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Description
            Text(
                text = "Choose the model that best fits your needs",
                fontSize = 16.sp,
                color = LocalCustomColors.current.bodyContentColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Model options
            modelOptions.forEachIndexed { index, model ->
                ModelOptionCard(
                    model = model,
                    isSelected = selectedModel == index,
                    onClick = {
                        selectedModel = index
                        isProgressVisible = true
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if(isCheckMarkVisible) {
                Row (
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    SavingBodyTextCheckMark()
                }
            }
        }
        // end of content
    }

    LaunchedEffect(isProgressVisible) {
        if (isProgressVisible) {
            delay(HIDE_TIME_ELAPSE)
            isProgressVisible = false
            isCheckMarkVisible = true
        } else {
            delay(HIDE_TIME_ELAPSE)
            isCheckMarkVisible = false
        }
    }
}
