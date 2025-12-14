# Eventful Android App

A modern Android application built with Jetpack Compose that provides a seamless way to discover and explore local events. The app features infinite scrolling, real-time data fetching, and a beautiful Material 3 design.

## 🚀 Features

### Core Functionality
- **Event Discovery**: Browse upcoming local events with rich details
- **Infinite Scrolling**: Seamlessly load more events as you scroll
- **Fast Initial Load**: Optimized to show content quickly (10 events initially)
- **Event Details**: Comprehensive event information with formatted dates and locations
- **Map Preview**: Interactive Google Maps preview showing event location
- **Smart Location Display**: Intelligent location prioritization (venue name → address → city → coordinates)
- **Navigation**: Smooth navigation between event list and detail screens

### Performance Optimizations
- **Pagination**: Loads events in chunks (10 initial, 20 for subsequent loads)
- **Smart Caching**: Local Room database for offline support
- **Efficient UI**: LazyColumn with optimized rendering
- **Background Loading**: Non-blocking data fetching with proper loading states

### User Experience
- **Material 3 Design**: Modern, accessible UI following Material Design guidelines
- **Edge-to-Edge**: Full-screen experience with proper status bar handling
- **Responsive Layout**: Adapts to different screen sizes
- **Error Handling**: Graceful error states with user-friendly messages
- **Loading States**: Clear feedback during data operations

## 🏗️ Architecture

The app follows **Clean Architecture** principles with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │ EventListScreen │  │ EventDetailScreen│  │ ViewModels  │ │
│  └─────────────────┘  └─────────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                           │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │ Use Cases       │  │ Models          │  │ Repository  │ │
│  │ (GetEvents)     │  │ (Event)         │  │ Interface   │ │
│  └─────────────────┘  └─────────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                       Data Layer                            │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │ Repository      │  │ Local Database  │  │ Remote API  │ │
│  │ Implementation  │  │ (Room)          │  │ (GraphQL)   │ │
│  └─────────────────┘  └─────────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Key Components

#### **Presentation Layer**
- **EventListScreen**: Main screen with infinite scrolling event list
- **EventDetailScreen**: Detailed event view with back navigation
- **ViewModels**: State management with Hilt dependency injection

#### **Domain Layer**
- **Use Cases**: Business logic for fetching events
- **Models**: Domain entities (Event)
- **Repository Interface**: Abstraction for data access

#### **Data Layer**
- **Repository Implementation**: Coordinates between local and remote data
- **Room Database**: Local caching with offline support
- **GraphQL API**: Remote data fetching with Apollo Client

## 🛠️ Technology Stack

### Core Technologies
- **Kotlin**: 100% Kotlin codebase
- **Jetpack Compose**: Modern declarative UI toolkit
- **Material 3**: Latest Material Design system
- **Hilt**: Dependency injection framework
- **Room**: Local database with SQLite
- **Apollo GraphQL**: GraphQL client for API communication

### Architecture Components
- **MVVM Pattern**: Model-View-ViewModel architecture
- **Clean Architecture**: Separation of concerns with clear boundaries
- **Repository Pattern**: Centralized data access abstraction
- **Use Cases**: Encapsulated business logic

### Performance & UX
- **LazyColumn**: Efficient list rendering
- **Infinite Scrolling**: Automatic content loading
- **Core Library Desugaring**: Support for modern Java APIs on older Android versions
- **Edge-to-Edge**: Full-screen immersive experience

## 📱 Screenshots & User Flow

### Event List Screen
- **Card-based Layout**: Each event displayed in a Material 3 card
- **Rich Information**: Title, date, and location at a glance
- **Infinite Scrolling**: Automatically loads more events when approaching the end
- **Loading Indicators**: Clear feedback during data operations

### Event Detail Screen
- **Comprehensive Details**: Full event information with formatted dates
- **Back Navigation**: Easy return to event list
- **Scrollable Content**: Handles long descriptions gracefully
- **Material 3 Top App Bar**: Consistent navigation experience

## 🔧 Setup & Installation

### Prerequisites
- **Android Studio**: Arctic Fox or later
- **JDK**: Version 11 or later
- **Android SDK**: API level 24+ (Android 7.0)
- **Gradle**: Version 8.13.0
- **Google Maps API Key**: For map preview functionality

### Google Maps Setup
To enable the map preview feature in event details:

1. **Get a Google Maps API Key**:
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable the "Maps SDK for Android" API
   - Create credentials (API Key)
   - Restrict the API key to your app's package name and SHA-1 fingerprint

2. **Configure the API Key**:
   - Open `local.properties` in the project root
   - Add your API key:
   ```properties
   GOOGLE_MAPS_API_KEY=your_actual_api_key_here
   ```

3. **Update API Key Restrictions**:
   - Package name: `com.eventful.app`
   - SHA-1 fingerprint: `1E:DE:DB:B4:8E:E5:A1:52:03:04:19:4C:F6:DE:6B:D5:EA:9B:4C:66`

4. **Security Note**: 
   - Never commit your actual API key to version control
   - The `local.properties` file is already in `.gitignore`
   - Consider using environment variables for CI/CD pipelines

### Build Configuration
```gradle
android {
    compileSdk = 36
    defaultConfig {
        minSdk = 24
        targetSdk = 35
    }
    compileOptions {
        coreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
```

### Dependencies
```gradle
// Core Android
implementation 'androidx.core:core-ktx:1.15.0'
implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.8.7'
implementation 'androidx.activity:activity-compose:1.9.3'

// Compose BOM
implementation platform('androidx.compose:compose-bom:2024.10.01')
implementation 'androidx.compose.ui:ui'
implementation 'androidx.compose.material3:material3'
implementation 'androidx.compose.ui:ui-tooling-preview'

// Navigation
implementation 'androidx.navigation:navigation-compose:2.8.5'

// Dependency Injection
implementation 'com.google.dagger:hilt-android:2.57.2'
implementation 'androidx.hilt:hilt-navigation-compose:1.2.0'

// Local Database
implementation 'androidx.room:room-runtime:2.8.1'
implementation 'androidx.room:room-ktx:2.8.1'

// GraphQL
implementation 'com.apollographql.apollo:apollo-runtime:4.1.0'

// Core Library Desugaring
coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:2.0.4'
```

## 🚀 Building & Running

### Clone and Build
```bash
git clone <repository-url>
cd eventful-android-app
./gradlew clean build
```

### Run on Device/Emulator
```bash
./gradlew installDebug
```

### Generate APK
```bash
./gradlew assembleDebug
```

## 🔄 Data Flow

### Initial Load
1. **App Launch** → ViewModel initializes
2. **Repository Call** → Fetches first 10 events from API
3. **Local Cache** → Stores events in Room database
4. **UI Update** → EventListScreen displays events

### Infinite Scrolling
1. **User Scrolls** → Approaches end of list (3 items from end)
2. **Trigger Detection** → LaunchedEffect detects scroll position
3. **Load More** → Repository fetches next 20 events
4. **Append Data** → New events added to existing list
5. **UI Update** → LazyColumn shows additional events

### Event Detail Navigation
1. **User Taps Event** → Navigation to detail screen
2. **ID Passing** → Event ID passed via navigation arguments
3. **Detail Fetch** → Repository fetches specific event details
4. **Display** → EventDetailScreen shows comprehensive information

## 🎨 UI/UX Design

### Material 3 Implementation
- **Dynamic Color**: Adapts to system theme
- **Typography**: Consistent text styles and hierarchy
- **Shapes**: Rounded corners and modern card designs
- **Elevation**: Proper depth and shadow system

### Responsive Design
- **Adaptive Layouts**: Works on phones and tablets
- **Edge-to-Edge**: Full-screen experience with proper insets
- **Status Bar Handling**: Content doesn't overlap with system UI
- **Orientation Support**: Handles both portrait and landscape

### Accessibility
- **Content Descriptions**: Screen reader support
- **Touch Targets**: Minimum 48dp touch targets
- **Color Contrast**: WCAG compliant color combinations
- **Semantic Labels**: Proper labeling for assistive technologies

## 🔧 Configuration

### API Endpoint
The app connects to the Eventful GraphQL API:
```
https://eventful-graphql-api-67457810649.us-central1.run.app/graphql
```

### GraphQL Queries
```graphql
# Initial load (10 events)
query AllEvents($limit: Int, $offset: Int) {
  allEvents(limit: $limit, offset: $offset) {
    id
    title
    description
    startTime
    endTime
    locationName
    address
    city
    stateProvince
    # ... other fields
  }
}

# Event details
query EventDetails($id: ID!) {
  eventById(id: $id) {
    id
    title
    description
    startTime
    endTime
    locationName
    address
    city
    # ... other fields
  }
}
```

## 🐛 Troubleshooting

### Common Issues

#### Build Errors
- **Gradle Sync Issues**: Clean and rebuild project
- **Dependency Conflicts**: Check version compatibility
- **Kotlin Version**: Ensure Kotlin 2.1.0 compatibility

#### Runtime Issues
- **Network Errors**: Check API endpoint availability
- **Database Issues**: Clear app data and restart
- **Memory Issues**: Monitor with Android Studio Profiler

### Debug Tools
- **Logcat**: Monitor app logs and errors
- **Network Inspector**: Debug API calls
- **Database Inspector**: View Room database contents
- **Layout Inspector**: Debug UI layout issues

## 📈 Performance Metrics

### Optimizations Implemented
- **Initial Load**: 10 events (50% faster than 20)
- **Pagination**: 20 events per subsequent load
- **Memory Usage**: Efficient with LazyColumn
- **Network**: Minimal API calls with smart caching

### Monitoring
- **Build Time**: ~40 seconds for clean build
- **APK Size**: Optimized with ProGuard
- **Memory Usage**: Monitored with Android Studio
- **Network Efficiency**: Reduced data transfer with pagination

## 🔮 Future Enhancements

## 🆕 Recent Updates

### GeoJSON Integration (Latest Update)
**Enhanced Location Handling**: Migrated from WKT to GeoJSON format for better mobile integration:

#### **What Changed**:
- **Before**: `geom` field was a WKT string: `"POINT(-73.5673 45.5017)"`
- **After**: `geom` field is a GeoJSON object: `{"type": "Point", "coordinates": [-73.5673, 45.5017]}`

#### **Benefits**:
- ✅ **Native JSON Parsing**: Direct parsing without complex regex operations
- ✅ **Type Safety**: Structured data with proper coordinate extraction
- ✅ **Performance**: Faster parsing and processing
- ✅ **Future-Ready**: Support for complex geometries (polygons, linestrings)
- ✅ **Standards Compliant**: RFC 7946 GeoJSON standard

#### **Implementation Details**:
```kotlin
data class Geometry(
    val type: String,
    val coordinates: List<Double>
) {
    val latitude: Double? get() = if (coordinates.size >= 2) coordinates[1] else null
    val longitude: Double? get() = if (coordinates.size >= 2) coordinates[0] else null
}
```

#### **Backward Compatibility**:
- **Fallback Support**: Still handles old WKT format during transition
- **Graceful Degradation**: Returns null for invalid geometry data
- **Error Handling**: Robust parsing with exception handling

### Planned Features
- **Search Functionality**: Filter events by title, category, location
- **Favorites**: Save events for later reference
- **Notifications**: Remind users about upcoming events
- **Maps Integration**: Show event locations on map
- **Offline Mode**: Enhanced offline experience
- **Dark Theme**: System theme adaptation

### Technical Improvements
- **Caching Strategy**: More sophisticated cache management
- **Image Loading**: Event images with proper caching
- **Analytics**: User behavior tracking
- **Crash Reporting**: Error monitoring and reporting
- **A/B Testing**: Feature experimentation framework

## 📄 License

This project is part of the Eventful platform for local event discovery.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📞 Support

For issues and questions:
- Check the troubleshooting section
- Review the API documentation
- Open an issue in the repository
