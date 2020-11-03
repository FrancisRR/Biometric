package com.francis.security

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_native_finger_print.*
import java.util.concurrent.Executor

class BiometricActivity : AppCompatActivity() {

    private val TAG by lazy { BiometricActivity::class.java.simpleName }
    private lateinit var biometricManager: BiometricManager
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricPromptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_finger_print)


        btBiometric.setOnClickListener {
            checkBiometric()
        }

    }


    private fun checkBiometric() {
        biometricManager = BiometricManager.from(this)

        //check biometric sensor present or not and biometric added or not in device
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                LogUtils.appLog(TAG, "Biometric is present")
                callBiometric()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                LogUtils.showToast("Please add fingerprint in security setting")

                //call system security setup window
                /*val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                startActivityForResult(enrollIntent, 655)*/

            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                LogUtils.showToast("Biometric authentication not available")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                LogUtils.showToast("Biometric authentication unavailable")
            }
        }
    }


    private fun callBiometric() {
        executor = ContextCompat.getMainExecutor(this);

        //Biometric dialog
        biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric")
            .setSubtitle("Authentication required")
            .setDescription("Please touch on fingerprint sensor")
            .setNegativeButtonText("Cancel")
            //.setDeviceCredentialAllowed(true)
            .build()


        //Biometric callback
        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    //error code 13 -> cancel button click
                    //error code 10 -> device back press
                    LogUtils.showToast("Biometric authentication error: $errString error code: $errorCode")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    LogUtils.showToast("Biometric authentication success")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    LogUtils.showToast("Biometric authentication failed")
                }
            })


        //Visible the biometric prompt to the user
        biometricPrompt.authenticate(biometricPromptInfo)

    }


}