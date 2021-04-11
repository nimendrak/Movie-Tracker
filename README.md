# Movie-Tracker
This was a Native Android Mobile Application which was developed as a Movie Tracker in IIT-L5-SEM2.

- Movie Tracker has following features :

    - Register Movies 
    - Add/ Remove Favorites Movies
    - Edit Registered Movies
    - Search Movies
    - Get Ratings from IMDB API
    
Every actions that user takes as mentioned in the above list, will be saved in SQLite Database. 

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
    │       │   │               ├── DisplayMovies.java
    │       │   │               ├── EditMovies.java
    │       │   │               ├── FavoriteMovies.java
    │       │   │               ├── MainActivity.java
    │       │   │               ├── Ratings.java
    │       │   │               ├── Search.java
    │       │   │               ├── SelectMovie.java
    │       │   │               └── utils
    │       │   │                   ├── CustomAdapter.java
    │       │   │                   │      ├── CustomAdapter.java
    │       │   │                   │      └── InjectImageToHolder.java
    │       │   │                   ├── FetchData.java
    │       │   │                   ├── Movie.java
    │       │   │                   └── MovieDatabase.java
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
    
