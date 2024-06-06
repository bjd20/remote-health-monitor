package com.example.healthmonitor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.healthmonitor.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"
    private var loginPage = true

    private lateinit var binding : ActivityLoginBinding

    private lateinit var auth: FirebaseAuth;
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("UsersData")

        binding.register.setOnClickListener {
            val emailTxt = binding.email.text.toString()
            val passwordTxt = binding.password.text.toString()

            if(emailTxt.isNotEmpty() && passwordTxt.isNotEmpty()){
                signupUser(emailTxt, passwordTxt)
            }else{
                Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT)

            }
        }

        binding.login.setOnClickListener {
            val emailTxt = binding.email.text.toString()
            val passwordTxt = binding.password.text.toString()

            if(emailTxt.isNotEmpty() && passwordTxt.isNotEmpty()){
                loginUser(emailTxt, passwordTxt)
            }else{
                Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT)

            }
        }

        binding.switchLink.setOnClickListener {
            loginPage = !loginPage
            if(loginPage){
                binding.register. visibility = View.GONE
                binding.login.visibility = View.VISIBLE
                binding.notRegText.text = getString(R.string.new_user)
                binding.switchLink.text = getString(R.string.register)
            }else{
                binding.login. visibility = View.GONE
                binding.register.visibility = View.VISIBLE
                binding.notRegText.text = getString(R.string.old_user)
                binding.switchLink.text = getString(R.string.log_in)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
//            reload()
//        updateUI(currentUser)

        }
    }

    private fun signupUser(email: String, password: String){

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail: success")
                    val user = auth.currentUser
                    Toast.makeText(this, "$user got registered", Toast.LENGTH_SHORT)

                    //update entries in db
                    val id = user!!.uid
                    val current_userRef = databaseReference.child(id)
                    val userData = UserData(id, email, password)
                    current_userRef.setValue(userData)
                    Toast.makeText(this@LoginActivity, "Signup Successful", Toast.LENGTH_SHORT)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }

        /*databaseReference.orderByChild("username").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()){
//                    val id = databaseReference.push().key
                    val id = auth.uid
                    val userData = UserData(id, email, password)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@LoginActivity, "Signup Successful", Toast.LENGTH_SHORT)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this@LoginActivity, "User Already Exists", Toast.LENGTH_SHORT)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error: ${error.message}", Toast.LENGTH_SHORT)
            }

        })*/
    }

    private fun loginUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "$user logged in", Toast.LENGTH_SHORT)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                    updateUI(user)
            } else {
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                    updateUI(null)
            }
        }
    }
}