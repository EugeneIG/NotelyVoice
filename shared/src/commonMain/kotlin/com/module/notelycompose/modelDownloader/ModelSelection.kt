package com.module.notelycompose.modelDownloader

import com.module.notelycompose.onboarding.data.PreferencesRepository
import kotlinx.coroutines.flow.first

const val NO_MODEL_SELECTION = -1
const val STANDARD_MODEL_SELECTION = 0
const val OPTIMIZED_MODEL_SELECTION = 1

data class TranscriptionModel(val name:String, val size:String, val description:String ){
    fun getModelUrl():String = "https://huggingface.co/ggerganov/whisper.cpp/resolve/main/$name"
    fun getModelDownloadMessage():String = "File size: approximately $size\n$description"
}

class ModelSelection(private val preferencesRepository: PreferencesRepository) {
    
    /**
     * Available Whisper models
     */
    private val models = listOf(
        TranscriptionModel(
            "ggml-base-en.bin",
            "139 MB",
            "English-optimized model (faster, smaller)"
        ),
        TranscriptionModel(
            "ggml-small.bin",
            "488 MB",
            "Multilingual model (supports 50+ languages)"
        )
    )
    
    /**
     * Get the appropriate model based on transcription language
     * @return The model to use
     */
    suspend fun getSelectedModel(): TranscriptionModel {
        val defaultLanguage = preferencesRepository.getDefaultTranscriptionLanguage().first()
        return when (defaultLanguage) {
            "en" -> models[0]
            else -> models[1]
        }
    }

    fun getDefaultTranscriptionModel() = models[0]


} 