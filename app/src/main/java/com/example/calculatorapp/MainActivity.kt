package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculatorapp.ui.theme.CalculatorappTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorappTheme {
                // Scaffold and content
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }
    var lastOperator by remember { mutableStateOf<String?>(null) }
    var operand1 by remember { mutableStateOf<Double?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display TextField for input and result
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter or use buttons") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display the result if available
        result?.let {
            Text(
                text = "Result: $it",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create buttons for the calculator
        CalculatorButtons(
            onDigitClicked = { digit ->
                input += digit
            },
            onOperatorClicked = { operator ->
                operand1 = input.toDoubleOrNull()
                if (operand1 == null) {
                    input = ""
                } else {
                    lastOperator = operator
                    input = ""
                }
            },
            onEqualClicked = {
                val operand2 = input.toDoubleOrNull()
                if (operand1 != null && operand2 != null && lastOperator != null) {
                    scope.launch {
                        result = calculate(operand1!!, operand2, lastOperator!!)
                        operand1 = null
                        lastOperator = null
                        input = ""
                    }
                }
            },
            onClearClicked = {
                input = ""
                result = null
                operand1 = null
                lastOperator = null
            }
        )
    }
}

// Function to handle calculations
fun calculate(operand1: Double, operand2: Double, operator: String): String {
    return when (operator) {
        "+" -> (operand1 + operand2).toString()
        "-" -> (operand1 - operand2).toString()
        "*" -> (operand1 * operand2).toString()
        "/" -> {
            if (operand2 == 0.0) "Error: Divide by zero"
            else (operand1 / operand2).toString()
        }
        else -> "Unknown operator"
    }
}

@Composable
fun CalculatorButtons(
    onDigitClicked: (String) -> Unit,
    onOperatorClicked: (String) -> Unit,
    onEqualClicked: () -> Unit,
    onClearClicked: () -> Unit
) {
    Column {
        Row {
            Button(onClick = { onDigitClicked("1") }, modifier = Modifier.weight(1f)) {
                Text("1")
            }
            Button(onClick = { onDigitClicked("2") }, modifier = Modifier.weight(1f)) {
                Text("2")
            }
            Button(onClick = { onDigitClicked("3") }, modifier = Modifier.weight(1f)) {
                Text("3")
            }
            Button(onClick = { onOperatorClicked("+") }, modifier = Modifier.weight(1f)) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = { onDigitClicked("4") }, modifier = Modifier.weight(1f)) {
                Text("4")
            }
            Button(onClick = { onDigitClicked("5") }, modifier = Modifier.weight(1f)) {
                Text("5")
            }
            Button(onClick = { onDigitClicked("6") }, modifier = Modifier.weight(1f)) {
                Text("6")
            }
            Button(onClick = { onOperatorClicked("-") }, modifier = Modifier.weight(1f)) {
                Text("-")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = { onDigitClicked("7") }, modifier = Modifier.weight(1f)) {
                Text("7")
            }
            Button(onClick = { onDigitClicked("8") }, modifier = Modifier.weight(1f)) {
                Text("8")
            }
            Button(onClick = { onDigitClicked("9") }, modifier = Modifier.weight(1f)) {
                Text("9")
            }
            Button(onClick = { onOperatorClicked("*") }, modifier = Modifier.weight(1f)) {
                Text("*")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = { onClearClicked() }, modifier = Modifier.weight(1f)) {
                Text("C")
            }
            Button(onClick = { onDigitClicked("0") }, modifier = Modifier.weight(1f)) {
                Text("0")
            }
            Button(onClick = { onEqualClicked() }, modifier = Modifier.weight(1f)) {
                Text("=")
            }
            Button(onClick = { onOperatorClicked("/") }, modifier = Modifier.weight(1f)) {
                Text("/")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorAppPreview() {
    CalculatorappTheme {
        CalculatorApp()
    }
}
