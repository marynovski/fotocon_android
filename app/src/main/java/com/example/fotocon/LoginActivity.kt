package com.example.fotocon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.fotocon.requests.LoginRequest
import com.example.fotocon.requests.RegisterRequest
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signUpBtn = findViewById<Button>(R.id.sign_up_btn);
        signUpBtn.setOnClickListener {
            redirectToSignUpActivity();
        }

        val signInBtn = findViewById<Button>(R.id.sign_in_btn);
        signInBtn.setOnClickListener {
            println("Click sign in!")
            val loginRequest = LoginRequest("test@example.com", "123456");
            GlobalScope.launch {
                sendLoginRequest(loginRequest)
            }
        }

    }


    private fun redirectToSignUpActivity()
    {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


    private suspend fun sendLoginRequest(request: LoginRequest): String {
        val client = HttpClient();
        val gson = Gson()
        println("Start signing in")
        val response: HttpResponse = client.post("http://192.168.8.104/fotocon/public/api/login") {
            contentType(ContentType.Text.Plain)
            headers {
                append("X-Requested-With", "XMLHttpRequest")
                append(HttpHeaders.Accept, "text/json")
            }
            setBody(gson.toJson(request))
        }

        println("Response GET: " + response)

        val stringBody: String = response.body()

        println("Response body: $stringBody")
        return stringBody
    }
}