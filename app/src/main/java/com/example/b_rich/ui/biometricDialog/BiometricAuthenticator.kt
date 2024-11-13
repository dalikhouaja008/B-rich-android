package com.example.b_rich.ui.biometricDialog

import android.content.Context

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

class BiometricAuthenticator (
    private val context: Context
){
    private lateinit var promptInfo : BiometricPrompt.PromptInfo
    private val biometricManager = BiometricManager.from(context)
    private lateinit var biometricPrompt: BiometricPrompt

    fun isBiometricAuthAvailable(): BiometricAuthStatus{
        return  when(biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)){
            BiometricManager.BIOMETRIC_SUCCESS->BiometricAuthStatus.READY
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->BiometricAuthStatus.NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED
            else -> BiometricAuthStatus.NOT_AVAILABLE
        }
    }

    fun promptBiometricAuth(
        title:String,
        subTitle:String,
        negativeButtonText:String,
        fragmentActivity:FragmentActivity,
        onSuccess:(result:BiometricPrompt.AuthenticationResult)->Unit,
        onFailed:()->Unit,
        onError: (Any?, Any?) -> Unit
    ){
        when(isBiometricAuthAvailable()){
            BiometricAuthStatus.NOT_AVAILABLE->{
                onError(BiometricAuthStatus.NOT_AVAILABLE.id,"Not available for this device")
                return
            }
            BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE->{
                onError(BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE.id,"Not available at this moment")
                return
            }
            BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED->{
                onError(BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED.id,"yOU SHOULD ADD A FINGERPRINT OR A FACE ID FIRST")
                return

            }
            else->Unit
        }

        biometricPrompt =BiometricPrompt(
            fragmentActivity,
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode,errString.toString())
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                   onFailed()
                }
            }
        )
        promptInfo= BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setNegativeButtonText(negativeButtonText)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

}