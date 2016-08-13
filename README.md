# popular-movies-stage-1

This is a simple android app that has a catalog of movies sorted by popularity or user rate. Clicking in the poster of the movie you can see more details.

![alt text](http://i.imgur.com/j9AM3NK.png?1) ![alt text](http://i.imgur.com/Nhdc89P.png?1)

The data and images of the movies are fetched from [themoviedb.org API](https://www.themoviedb.org/documentation/api).

To run the app and get data from themoviedb.org API, you need to get an API Key, it can be obtained by [registering](https://www.themoviedb.org/account/signup) on the themoviedb.org site. You need to put your API Key in the variable API_KEY, it is located in the file MovieCatalogFragment.java (package com.example.marcoaurelio.popularmovies), inside the inner class FetchMoviesDataTask, inside the method doInBackground:

![alt text](http://i.imgur.com/SjzbrMb.png)
