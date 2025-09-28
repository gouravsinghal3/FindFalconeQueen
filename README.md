# FindQueen Android App 🚀

A modern Android application built with Jetpack Compose that implements the "Finding Falcone" challenge. Help find Queen Al Falcone by selecting planets and vehicles to search across the galaxy!

## 📱 App Overview

FindQueen is a strategy game where you need to:
- Select 4 different planets to search for Queen Al Falcone
- Choose appropriate vehicles for each planet based on distance and availability
- Submit your selections to find if the Queen is on any of the selected planets

## 🏗️ Architecture

This app follows **Clean Architecture** principles with **MVVM** pattern and uses modern Android development practices.

### Architecture Layers

```
┌─────────────────────────────────────────┐
│             Presentation Layer          │
│  ┌─────────────┐ ┌─────────────────────┐│
│  │   Compose   │ │    ViewModels       ││
│  │     UI      │ │  (State Management) ││
│  └─────────────┘ └─────────────────────┘│
└─────────────────────────────────────────┘
                    │
┌─────────────────────────────────────────┐
│              Domain Layer               │
│  ┌─────────────┐ ┌─────────────────────┐│
│  │   Models    │ │    Use Cases        ││
│  │ (Entities)  │ │ (Business Logic)    ││
│  └─────────────┘ └─────────────────────┘│
└─────────────────────────────────────────┘
                    │
┌─────────────────────────────────────────┐
│               Data Layer                │
│  ┌─────────────┐ ┌─────────────────────┐│
│  │ Repository  │ │    API Service      ││
│  │(Data Source)│ │   (Remote Data)     ││
│  └─────────────┘ └─────────────────────┘│
└─────────────────────────────────────────┘
```

## 📁 Project Structure

```
app/src/main/java/com/example/findqueen/
├── 📱 presentation/
│   ├── screen/
│   │   ├── HomeScreen.kt          # Main game screen
│   │   ├── SelectionScreen.kt     # Planet/Vehicle selection
│   │   └── ResultScreen.kt        # Mission results
│   ├── viewmodel/
│   │   └── FindQueenViewModel.kt  # UI state management
│   └── navigation/
│       ├── Screen.kt              # Navigation routes
│       └── FindQueenNavigation.kt # Navigation graph
├── 🏢 domain/
│   ├── model/
│   │   ├── Planet.kt              # Planet entity
│   │   ├── Vehicle.kt             # Vehicle entity
│   │   ├── Selection.kt           # Game state models
│   │   └── FindingResult.kt       # Result entity
│   ├── repository/
│   │   └── FalconeRepository.kt   # Repository interface
│   └── usecase/
│       ├── GetPlanetsUseCase.kt   # Fetch planets logic
│       ├── GetVehiclesUseCase.kt  # Fetch vehicles logic
│       └── FindFalconeUseCase.kt  # Search logic
├── 💾 data/
│   ├── remote/
│   │   ├── api/
│   │   │   └── FalconeApiService.kt # Retrofit API interface
│   │   └── dto/
│   │       ├── PlanetDto.kt        # API response models
│   │       ├── VehicleDto.kt
│   │       ├── TokenDto.kt
│   │       ├── FindFalconeRequest.kt
│   │       └── FindFalconeResponse.kt
│   └── repository/
│       └── FalconeRepositoryImpl.kt # Repository implementation
├── 🔧 di/
│   ├── NetworkModule.kt           # Networking DI
│   └── RepositoryModule.kt        # Repository DI
└── 🎨 ui/theme/
    ├── Color.kt                   # App colors
    ├── Theme.kt                   # Material theme
    └── Type.kt                    # Typography
```

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **Coroutines** - Asynchronous programming
- **Flow & StateFlow** - Reactive streams

### Architecture Components
- **MVVM** - Architectural pattern
- **Clean Architecture** - Separation of concerns
- **Use Cases** - Business logic encapsulation
- **Repository Pattern** - Data access abstraction

### Dependency Injection
- **Hilt** - Dependency injection framework

### Networking
- **Retrofit** - HTTP client
- **OkHttp** - HTTP/HTTP2 client
- **Gson** - JSON serialization

### Navigation
- **Navigation Compose** - Compose navigation

### Testing
- **JUnit 4** - Unit testing framework
- **Mockito** - Mocking framework
- **Compose UI Test** - UI testing
- **Coroutines Test** - Async testing
- **Turbine** - Flow testing
- **MockK** - Kotlin mocking library

## 🚀 Getting Started

### Prerequisites
- Android Studio Flamingo or later
- JDK 11 or later
- Android SDK 24+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd FindQueen
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the FindQueen directory

3. **Sync and Build**
   ```bash
   ./gradlew build
   ```

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click "Run" in Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

## 🎮 How to Use the App

### 1. Home Screen
- View game instructions and current progress
- Start a new game or continue selection
- See total time taken for your selections

### 2. Selection Process
For each of the 4 destinations:

**Step 1: Choose a Planet**
- Browse available planets
- View distance information
- Select a planet (each planet can only be selected once)

**Step 2: Choose a Vehicle**
- View available vehicles with specifications:
  - **Max Distance**: Maximum range the vehicle can travel
  - **Speed**: Affects time calculation (Time = Distance ÷ Speed)
  - **Available Count**: How many vehicles of this type are available
- Select a vehicle that can reach your chosen planet

**Step 3: Confirm Selection**
- Review your planet and vehicle choice
- Confirm to proceed to the next destination

### 3. Finding Falcone
- Once all 4 destinations are selected, tap "Find Falcone!"
- The app will search for Queen Al Falcone
- View results showing if the mission was successful

### 4. Mission Results
- **Success**: Congratulations! Shows which planet had the Queen
- **Failure**: Better luck next time! The Queen wasn't found
- **Error**: Network or server issues occurred
- View mission summary with all your selections
- Start a new game to try again

## 🧪 Testing

The app includes comprehensive test coverage:

### Unit Tests
```bash
./gradlew test
```

**Test Coverage:**
- ✅ ViewModels (State management)
- ✅ Use Cases (Business logic)
- ✅ Repository (Data access)
- ✅ Domain Models (Entity logic)
- ✅ Data Mapping (DTO conversions)

### UI Tests
```bash
./gradlew connectedAndroidTest
```

**UI Test Coverage:**
- ✅ Home Screen interactions
- ✅ Selection Screen workflows
- ✅ Result Screen displays
- ✅ Navigation flows
- ✅ Error state handling
- ✅ Loading state management

### Test Files Structure
```
app/src/test/                          # Unit Tests
├── domain/model/GameStateTest.kt
├── domain/usecase/
├── data/repository/
├── presentation/viewmodel/
└── data/remote/dto/

app/src/androidTest/                    # UI Tests
├── presentation/screen/
│   ├── HomeScreenTest.kt
│   ├── SelectionScreenTest.kt
│   └── ResultScreenTest.kt
└── presentation/navigation/NavigationTest.kt
```

## 🔧 Configuration

### API Configuration
The app uses the FindFalcone API:
- **Base URL**: `http://findfalcone.geektrust.com/`
- **Endpoints**:
  - `GET /planets` - Fetch available planets
  - `GET /vehicles` - Fetch available vehicles
  - `POST /token` - Get authentication token
  - `POST /find` - Submit search request

### Network Security
For development, the app includes:
- Network security configuration for HTTP traffic
- SSL certificate handling
- Connection timeout settings (30 seconds)

## 🐛 Troubleshooting

### Common Issues

1. **Build Errors**
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

2. **Network Issues**
   - Check internet connection
   - Verify API endpoints are accessible
   - Review network security configuration

3. **Test Failures**
   - Ensure all dependencies are synced
   - Check for conflicting JUnit versions
   - Run tests individually to isolate issues

## 📝 Game Rules

1. **Planet Selection**: Must select exactly 4 different planets
2. **Vehicle Constraints**:
   - Each vehicle type has limited quantity
   - Vehicles have maximum distance limitations
   - Once used, vehicle count decreases
3. **Time Calculation**: Time = Distance ÷ Speed
4. **Objective**: Find Queen Al Falcone in minimum time

## 🎨 UI/UX Features

- **Material 3 Design** - Modern, clean interface
- **Responsive Layout** - Works on different screen sizes
- **Loading States** - Clear feedback during network calls
- **Error Handling** - User-friendly error messages
- **Progress Tracking** - Visual progress indicators
- **Intuitive Navigation** - Easy to use flow

## 📄 License

This project is created for educational purposes as part of the GeekTrust coding challenge.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

---

**Happy Planet Hunting! 🌟**

*Find Queen Al Falcone and save the galaxy!*
