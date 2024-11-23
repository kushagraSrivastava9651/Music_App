package com.example.musicappui.ui.theme
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicappui.R

@Composable
fun SubscriptionView() {
    val backgroundColor = MaterialTheme.colors.background

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).background(backgroundColor)
    ) {
        // Header Section
        Text(text = "Subscription Plans", style = MaterialTheme.typography.h4)

        // Subscription Plans Section
        SubscriptionPlan(name = "Basic", price = "$9.99/month")
        SubscriptionPlan(name = "Standard", price = "$19.99/month")
        SubscriptionPlan(name = "Premium", price = "$29.99/month")

        // Payment Methods Section
        Text(
            text = "Accepted Payment Methods",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp)
        )
        PaymentMethods()
    }
}

@Composable
fun SubscriptionPlan(name: String, price: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.h5)
            Text(text = price, style = MaterialTheme.typography.body1)
            Button(onClick = { /* Subscribe Logic */ }) {
                Text(text = "Subscribe")
            }
        }
    }
}

@Composable
fun PaymentMethods() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        // Add icons/logos for payment methods (e.g., Visa, Mastercard, PayPal)
        Icon(
            painter = painterResource(id = R.drawable.visa),
            contentDescription = "Visa",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            painter = painterResource(id = R.drawable.upi),
            contentDescription = "Mastercard",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            painter = painterResource(id = R.drawable.paypal),
            contentDescription = "PayPal",
            modifier = Modifier.size(48.dp)
        )
    }
}
