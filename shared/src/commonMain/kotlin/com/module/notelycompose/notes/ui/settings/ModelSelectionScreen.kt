package com.module.notelycompose.notes.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.notes.ui.detail.AndroidNoteTopBar
import com.module.notelycompose.notes.ui.detail.IOSNoteTopBar
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.platform.getPlatform

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
            description = "Faster performance, smaller size.\nDoes not support Hindi.",
            size = "133 MB"
        ),
        ModelOption(
            title = "Optimized model (multilingual)",
            description = "Supports Hindi & other multiple languages with super high accuracy.",
            size = "488 MB"
        )
    )

    var selectedModel by remember { mutableIntStateOf(0) } // Standard model selected by default

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Title
            Text(
                text = "Model Selection",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Subtitle
            Text(
                text = "AI Model",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Description
            Text(
                text = "Choose the model that best fits your needs",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Model options
            modelOptions.forEachIndexed { index, model ->
                ModelOptionCard(
                    model = model,
                    isSelected = selectedModel == index,
                    onClick = { selectedModel = index },
                    modifier = Modifier.padding(bottom = 16.dp),
                    hasSelection = true
                )
            }
        }
        // end of content
    }
}
