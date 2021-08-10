### Real Estate app

App that displays houses for sale. It displays an image, their price, address, amount of bedrooms and bathrooms, size and distance based on the user's current location.
Tech stack: MVVM, Retrofit, Glide and Google Maps API. 

When the app gets started a splash screen is displayed

![](assets/splashscreen.png)

The first thing you need to do is allow or deny access to the device's location. It is necessary if you want to know the distance between you and the house.

![](assets/permissions.png)

This view displays an overview of the houses with a search bar on top.

![](assets/overview.png)

You can use the search bar to filter for houses by address or zip code. If no result matches, this screen is displayed.

![](assets/Noresult.png)

When you click on a item, you move to detailed view with a bigger picture, a description and a marker with its location in the map.

![](assets/detail.png)

If you click on the map inside the detailed view you'll get redirected to Google Maps to get directions

![](assets/directions.png)

The other bottom navigation button is just a static view with some fictional info about the development team

![](assets/info.png)

