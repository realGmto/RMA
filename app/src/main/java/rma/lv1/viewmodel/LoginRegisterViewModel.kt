package rma.lv1.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginRegisterViewModel : ViewModel() {

    private val _loginResult = MutableStateFlow<Boolean>(false)
    val loginResult:StateFlow<Boolean> = _loginResult.asStateFlow()

    fun signIn(context: Context, email: String, password: String){
        viewModelScope.launch {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _loginResult.value = true
                        // Prijavljeno uspješno
                        Toast.makeText(context, "Logged in successfully",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        _loginResult.value = false
                        // Prijavljivanje neuspješno
                        Toast.makeText(context, "Login failed",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    fun register(context: Context, email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,
            password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registracija uspješna
                    Toast.makeText(context, "Registered successfully",
                        Toast.LENGTH_SHORT).show()
                } else {
                    // Registracija neuspješna
                    Toast.makeText(context, "Registration failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}