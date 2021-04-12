# Movie-Tracker
This was a Native Android Mobile Application which was developed as a Movie Tracker in IIT-L5-SEM2.

- Movie Tracker has following features :

    - Register Movies 
    - Add/Remove Favorites Movies
    - Edit Registered Movies
    - Search Movies in Device Database
    - Get Movie Title/Ratings/Images from IMDB API
    
Application uses a SQLite Database in order to store movie data. 

## Essential Directory Layout

    .
    ├── app
    │   ├── build
    │   ├── build.gradle
    │   ├── libs
    │   └── src
    │       ├── androidTest
    │       ├── main
    │       │   ├── AndroidManifest.xml
    │       │   ├── java
    │       │   │   └── com
    │       │   │       └── example
    │       │   │           └── nimendra
    │       │   │               ├── utils
    │       │   │               │   ├── CustomAdapter.java
    │       │   │               │   ├── FetchData.java
    │       │   │               │   ├── MovieDatabase.java
    │       │   │               │   ├── MovieModel.java
    │       │   │               │   └── ShowSnackBar.java
    │       │   │               ├── DisplayMovies.java
    │       │   │               ├── EditMovies.java
    │       │   │               ├── FavoriteMovies.java
    │       │   │               ├── MainActivity.java
    │       │   │               ├── Ratings.java
    │       │   │               ├── Search.java
    │       │   │               └── SelectMovie.java
    │       │   └── res
    │       │       ├── drawable
    │       │       └── values
    │       │           ├── colors.xml
    │       │           ├── dimen.xml
    │       │           ├── strings.xml
    │       │           └── styles.xml
    │       └── test
    ├── build.gradle
    ├── gradle
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    ├── local.properties
    └── settings.gradle
    
## Important Notes

- IMDB API key is defined on res -> values -> strings as ```MY_API_KEY```. In order to do get data from IMDB, you need a valid API key.