package com.example.findqueen.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.findqueen.presentation.navigation.Screen
import com.example.findqueen.presentation.viewmodel.FindQueenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: FindQueenViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Finding Falcone!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Select planets you want to search in:",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "You need to select 4 planets and vehicles to search for Queen Al Falcone",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            if (uiState.isLoading) {
                CircularProgressIndicator()
                Text(
                    "Loading planets and vehicles...",
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else if (uiState.error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Error",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text=uiState.error.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.clearError() }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            } else {
                // Game progress
                if (uiState.gameState.selections.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                "Progress: ${uiState.gameState.selections.size}/4 destinations selected",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "Total time: ${uiState.gameState.totalTime}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            uiState.gameState.selections.forEachIndexed { index, selection ->
                                Text(
                                    "${index + 1}. ${selection.planet.name} (${selection.vehicle.name})",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Action buttons
                if (uiState.gameState.isComplete) {
                    Button(
                        onClick = { 
                            viewModel.findFalcone {
                                navController.navigate(Screen.Result.route)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Find Falcone!")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedButton(
                        onClick = { viewModel.resetGame() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Start Over")
                    }
                } else {
                    Button(
                        onClick = { 
                            navController.navigate(
                                Screen.SelectionStep.createRoute(uiState.currentStep)
                            ) 
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (uiState.gameState.selections.isEmpty()) 
                                "Start Selection" 
                            else 
                                "Continue Selection (${uiState.currentStep + 1}/4)"
                        )
                    }
                    
                    if (uiState.gameState.selections.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedButton(
                            onClick = { viewModel.resetGame() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Start Over")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Instructions
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Instructions:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Select 4 different planets to search")
                    Text("• Choose appropriate vehicles for each planet")
                    Text("• Vehicles have limited range and quantity")
                    Text("• Time = Distance ÷ Speed")
                }
            }
        }
    }
}
