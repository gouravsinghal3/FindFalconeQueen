package com.example.findqueen.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.findqueen.R
import com.example.findqueen.domain.model.Planet
import com.example.findqueen.domain.model.Vehicle
import com.example.findqueen.presentation.navigation.Screen
import com.example.findqueen.presentation.viewmodel.FindQueenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionScreen(
    navController: NavController,
    viewModel: FindQueenViewModel,
    step: Int
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedPlanet by remember { mutableStateOf<Planet?>(null) }
    var selectedVehicle by remember { mutableStateOf<Vehicle?>(null) }
    
    // Filter out already selected planets
    val selectedPlanetNames = uiState.gameState.selections.map { it.planet.name }
    val availablePlanets = uiState.planets.filter { it.name !in selectedPlanetNames }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Destination ${step + 1} of 4") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (uiState.error != null) {
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
                            stringResource(R.string.error),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            uiState.error ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            LazyColumn {
                item {
                    Text(
                        stringResource(R.string.select_planet),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                item {
                    Column(
                        modifier = Modifier.selectableGroup()
                    ) {
                        availablePlanets.forEach { planet ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .selectable(
                                        selected = selectedPlanet == planet,
                                        onClick = {
                                            selectedPlanet = planet
                                            selectedVehicle =
                                                null // Reset vehicle when planet changes
                                        },
                                        role = Role.RadioButton
                                    ),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedPlanet == planet) 
                                        MaterialTheme.colorScheme.primaryContainer 
                                    else 
                                        MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedPlanet == planet,
                                        onClick = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            planet.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            "Distance: ${planet.distance} megamiles",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (selectedPlanet != null) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            "Select Vehicle:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    
                    item {
                        Column(
                            modifier = Modifier.selectableGroup()
                        ) {
                            uiState.vehicles.forEach { vehicle ->
                                val availableCount = uiState.gameState.getAvailableCount(vehicle.name)
                                val canReachPlanet = selectedPlanet!!.distance <= vehicle.maxDistance
                                val isAvailable = availableCount > 0
                                val isSelectable = canReachPlanet && isAvailable
                                
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .selectable(
                                            selected = selectedVehicle == vehicle,
                                            onClick = {
                                                if (isSelectable) {
                                                    selectedVehicle = vehicle
                                                }
                                            },
                                            role = Role.RadioButton,
                                            enabled = isSelectable
                                        ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = when {
                                            selectedVehicle == vehicle -> MaterialTheme.colorScheme.primaryContainer
                                            !isSelectable -> MaterialTheme.colorScheme.surfaceVariant
                                            else -> MaterialTheme.colorScheme.surface
                                        }
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = selectedVehicle == vehicle,
                                            onClick = null,
                                            enabled = isSelectable
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                vehicle.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = if (isSelectable) 
                                                    MaterialTheme.colorScheme.onSurface 
                                                else 
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                "Max Distance: ${vehicle.maxDistance} | Speed: ${vehicle.speed}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                "Available: $availableCount",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (isAvailable) 
                                                    MaterialTheme.colorScheme.primary 
                                                else 
                                                    MaterialTheme.colorScheme.error
                                            )
                                            if (selectedPlanet != null) {
                                                val timeTaken = selectedPlanet!!.distance / vehicle.speed
                                                Text(
                                                    "Time: $timeTaken",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            
                                            if (!canReachPlanet) {
                                                Text(
                                                    "Cannot reach this planet",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                if (selectedPlanet != null && selectedVehicle != null) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                viewModel.selectPlanetAndVehicle(selectedPlanet!!, selectedVehicle!!)
                                if (uiState.gameState.selections.size + 1 < 4) {
                                    navController.navigate(
                                        Screen.SelectionStep.createRoute(step + 1)
                                    )
                                } else {
                                    navController.popBackStack(Screen.Home.route, false)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.confirm_selection))
                        }
                    }
                }
            }
        }
    }
}
