package com.example.fotocon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.fotocon.requests.RegisterRequest
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val backToSignInBtn = findViewById<Button>(R.id.back_to_sign_in_btn);
        backToSignInBtn.setOnClickListener {
            backToSignInActivity();
        }

        val singUpBtn = findViewById<Button>(R.id.sign_up_btn);
        singUpBtn.setOnClickListener {

            val registerRequest = RegisterRequest("test@example.com", "123456", "123456");
            GlobalScope.launch {
                sendRegisterRequest(registerRequest)
            }


        }
    }


    private fun backToSignInActivity()
    {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    private suspend fun sendRegisterRequest(request: RegisterRequest)
    {
        val client = HttpClient();
        val gson = Gson()
        val response: HttpResponse = client.post("http://192.168.8.104/fotocon/public/api/register") {
            contentType(ContentType.Application.Json)
            headers {
                append("X-Requested-With", "XMLHttpRequest")
            }
            setBody(gson.toJson(request))
        }

        println(response)
    }
}