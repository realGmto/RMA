package rma.lv1.viewmodel

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import rma.lv1.model.PersonModel

class BMIViewModel:ViewModel() {
    private val db = Firebase.firestore

    private val _bmi = MutableLiveData<Double>()
    val bmi: LiveData<Double> = _bmi
    private val _person = MutableLiveData<PersonModel>()
    val person: LiveData<PersonModel> = _person

    fun fetchPerson(){
        var height:Double?
        var weight:Double?
        var steps:Int

        db.collection("BMI").document("mKWvFApbcOYLnbnkW2vc")
            .get()
            .addOnSuccessListener { result ->
                weight = result.getDouble("weight")!!
                height = result.getDouble("height")!!
                steps = result.getDouble("steps")!!.toInt()
                _person.value = PersonModel(height,weight,steps)
            }
            .addOnFailureListener {e ->
                Log.e("MainActivity", "Error Getting data: $e")
            }
    }
    fun updatePerson(height: Double?,weight: Double?){
        db.collection("BMI").document("mKWvFApbcOYLnbnkW2vc")
            .update(mapOf(
                "weight" to weight,
                "height" to height
            ))
            .addOnSuccessListener {
                _person.value = PersonModel(height,weight,_person.value?.steps)
            }
            .addOnFailureListener { e ->
                // Update failed (handle error, e.g., show an error message)
                Log.e("MainActivity", "Error updating Person: $e")
            }
    }

    fun CalculateBMI(): Float?{
        return (_person.value?.weight!! / (_person.value?.height!! * _person.value?.height!!)).toFloat()
    }
}