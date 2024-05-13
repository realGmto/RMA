package rma.lv1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import rma.lv1.model.BMIModel

class BMIViewModel:ViewModel() {
    private val db = Firebase.firestore

    private val _bmi = MutableLiveData<BMIModel>()
    val bmi: LiveData<BMIModel> = _bmi

    fun fetchBMImodel(){
        var height:Double?
        var weight:Double?
        var BMI: Double?

        db.collection("BMI").document("mKWvFApbcOYLnbnkW2vc")
            .get()
            .addOnSuccessListener { result ->
                weight = result.getDouble("weight")!!
                height = result.getDouble("height")!!
                BMI = weight!! / (height!! * height!!)
                _bmi.value = BMIModel(height,weight,BMI)
            }
            .addOnFailureListener {e ->
                Log.e("MainActivity", "Error Getting data: $e")
            }
    }
    fun updatePerson(height: Double?,weight: Double?){
        var BMI: Double?
        db.collection("BMI").document("mKWvFApbcOYLnbnkW2vc")
            .update(mapOf(
                "weight" to weight,
                "height" to height
            ))
            .addOnSuccessListener {
                BMI = weight!! / (height!! * height!!)
                _bmi.value = BMIModel(height,weight, BMI)
            }
            .addOnFailureListener { e ->
                // Update failed (handle error, e.g., show an error message)
                Log.e("MainActivity", "Error updating Person: $e")
            }
    }
}