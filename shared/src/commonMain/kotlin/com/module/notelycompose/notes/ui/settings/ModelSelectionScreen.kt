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
import com.module.notelycompose.modelDownloader.NO_MODEL_SELECTION
import com.module.notelycompose.notes.ui.detail.AndroidNoteTopBar
import com.module.notelycompose.notes.ui.detail.IOSNoteTopBar
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.onboarding.data.PreferencesRepository
import com.module.notelycompose.platform.getPlatform
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private const val HIDE_TIME_ELAPSE = 1500L

data class ModelOption(
    val title: String,
    val description: String,
    val size: String = ""
)

@Composable
fun ModelSelectionScreen(
    navigateBack: () -> Unit,
    preferencesRepository: PreferencesRepository = koinInject()
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
    var modelSavedSelection by remember { mutableStateOf(NO_MODEL_SELECTION) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        modelSavedSelection = preferencesRepository.getModelSelection().first()
    }

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
                .padding(horizontal = 24.dp, vertical = 12.dp)
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
                    isSelected = if(modelSavedSelection != NO_MODEL_SELECTION) {
                        modelSavedSelection == index
                    } else {
                        selectedModel == index
                    },
                    onClick = {
                        selectedModel = index
                        coroutineScope.launch {
                            preferencesRepository.setModelSelection(selectedModel)
                        }
                        navigateBack()
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
        // end of content
    }
}
