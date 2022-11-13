package com.example.fotocon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fotocon.requests.RegisterRequest
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar = findViewById<MaterialToolbar>(R.id.sign_up_toolbar);
        toolbar.setNavigationOnClickListener {
            backToSignInActivity();
        }

        val signUpBtn = findViewById<Button>(R.id.sign_up_btn);
        signUpBtn.setOnClickListener {
            val emailInput = findViewById<EditText>(R.id.email_input);
            val passwordInput = findViewById<EditText>(R.id.password_input);
            val passwordRepeatInput = findViewById<EditText>(R.id.password_repeat_input);

            val email: String = (emailInput.text).toString();
            val password: String = (passwordInput.text).toString();
            val passwordRepeat: String = (passwordRepeatInput.text).toString();


            val registerRequest = RegisterRequest(email, password, passwordRepeat);
            GlobalScope.launch {
                val response = sendRegisterRequest(registerRequest)

                withContext(Dispatchers.Main) {
                    if (response.status.value === 422) {
                        val responseJson = JSONObject(response.bodyAsText());
                        println(responseJson.getJSONObject("errors"))

                        val errors = responseJson.getJSONObject("errors");

                        val emailInputLayout = findViewById<TextInputLayout>(R.id.email_input_layout);
                        if (errors.has("email")) {
                            emailInputLayout.helperText =
                                errors.getJSONArray("email").getString(0).toString();
                        } else {
                            emailInputLayout.helperText = "";
                        }

                        val passwordInputLayout = findViewById<TextInputLayout>(R.id.password_input_layout);
                        if (errors.has("password")) {
                            passwordInputLayout.helperText =
                                errors.getJSONArray("password").getString(0).toString();
                        } else {
                            passwordInputLayout.helperText = "";
                        }
                    } else if (response.status.value === 201) {
                    } else {

                    }
                }
            }
        }
    }


    private fun backToSignInActivity()
    {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    private suspend fun sendRegisterRequest(request: RegisterRequest): HttpResponse {
        val client = HttpClient();
        val gson = Gson()
        val response: HttpResponse = client.post("http://192.168.8.104/fotocon/public/api/register") {
            contentType(ContentType.Application.Json)
            headers {
                append("X-Requested-With", "XMLHttpRequest")
            }
            setBody(gson.toJson(request))
        }

        return response;
    }
}