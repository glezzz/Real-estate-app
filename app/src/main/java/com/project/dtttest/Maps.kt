// package com.project.dtttest
//
// import android.Manifest
// import android.content.pm.PackageManager
// import android.location.Location
// import android.widget.Toast
// import androidx.core.app.ActivityCompat
// import androidx.core.content.ContextCompat
// import com.google.android.gms.location.FusedLocationProviderClient
// import com.google.android.gms.maps.CameraUpdateFactory
// import com.google.android.gms.maps.GoogleMap
// import com.google.android.gms.maps.OnMapReadyCallback
// import com.google.android.gms.maps.SupportMapFragment
// import com.google.android.gms.maps.model.LatLng
// import com.google.android.gms.maps.model.MarkerOptions
// import com.google.android.gms.tasks.Task
// import com.project.dtttest.ui.fragments.HouseDetailFragment
//
// class Maps : HouseDetailFragment(), OnMapReadyCallback {
//
//     // private lateinit var map: GoogleMap
//     //
//     // /**
//     //  * Initializes Google Maps fragment for house location
//     //  */
//     // fun initGoogleMap() {
//     //     val mapFragment = childFragmentManager
//     //         .findFragmentById(R.id.frMap) as SupportMapFragment?
//     //     mapFragment?.getMapAsync(this)
//     // }
//     //
//     // override fun onMapReady(googleMap: GoogleMap) {
//     //     map = googleMap
//     //     createMarker()
//     //     enableUserLocation()
//     //    //getLocation()
//     // }
//     //
//     // /**
//     //  * Creates marker for exact location of house
//     //  */
//     // private fun createMarker() {
//     //     val house = args.house
//     //     val houseLocation = LatLng(house.latitude.toDouble(), house.longitude.toDouble())
//     //     map.addMarker(MarkerOptions().position(houseLocation))
//     //     map.animateCamera(
//     //         CameraUpdateFactory.newLatLngZoom(houseLocation, 12f),
//     //         1,
//     //         null
//     //     )
//     // }
//
//     // Stretch image in view
//
//     // ImageView imageView = (ImageView)findViewById(R.id.imageView);
//     // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.foo);
//     //
//     // int imageWidth = bitmap.getWidth();
//     // int imageHeight = bitmap.getHeight();
//     //
//     // int newWidth = getScreenWidth(); //this method should return the width of device screen.
//     // float scaleFactor = (float)newWidth/(float)imageWidth;
//     // int newHeight = (int)(imageHeight * scaleFactor);
//     //
//     // bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
//     // imageView.setImageBitmap(bitmap);
//
//
//     private fun calculateDistance(
//         latOwnLocation: Double,
//         lonOwnLocation: Double,
//         latHouseLocation: Double,
//         lonHouseLocation: Double
//     ): Float {
//         val distance = FloatArray(2)
//         Location.distanceBetween(
//             latOwnLocation, lonOwnLocation,
//             latHouseLocation, lonHouseLocation, distance
//         )
//         return distance[0]
//     }
//
//     /**
//      * Permission to get current location
//      */
//     private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
//         requireContext(),
//         Manifest.permission.ACCESS_FINE_LOCATION
//     ) == PackageManager.PERMISSION_GRANTED
//
//     private fun enableUserLocation() {
//         if (!::map.isInitialized) return
//         if (isLocationPermissionGranted()) {
//             if (ActivityCompat.checkSelfPermission(
//                     requireContext(),
//                     Manifest.permission.ACCESS_FINE_LOCATION
//                 ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                     requireContext(),
//                     Manifest.permission.ACCESS_COARSE_LOCATION
//                 ) != PackageManager.PERMISSION_GRANTED
//             ) {
//                 // TODO: Consider calling
//                 //    ActivityCompat#requestPermissions
//                 // here to request the missing permissions, and then overriding
//                 //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                 //                                          int[] grantResults)
//                 // to handle the case where the user grants the permission. See the documentation
//                 // for ActivityCompat#requestPermissions for more details.
//                 return
//             }
//             map.isMyLocationEnabled = true
//         } else {
//             requestLocationPermission()
//         }
//     }
//
//     private fun requestLocationPermission() {
//         if (ActivityCompat.shouldShowRequestPermissionRationale(
//                 requireActivity(),
//                 Manifest.permission.ACCESS_FINE_LOCATION
//             )
//         ) {
//             Toast.makeText(
//                 requireContext(),
//                 "Ve a ajustes y acepta los permisos",
//                 Toast.LENGTH_SHORT
//             ).show()
//         } else {
//             ActivityCompat.requestPermissions(
//                 requireActivity(),
//                 arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                 LOCATION_REQUEST_CODE
//             )
//         }
//     }
//
//     override fun onRequestPermissionsResult(
//         requestCode: Int,
//         permissions: Array<out String>,
//         grantResults: IntArray
//     ) {
//         when (requestCode) {
//             LOCATION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                 if (ActivityCompat.checkSelfPermission(
//                         requireContext(),
//                         Manifest.permission.ACCESS_FINE_LOCATION
//                     ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                         requireContext(),
//                         Manifest.permission.ACCESS_COARSE_LOCATION
//                     ) != PackageManager.PERMISSION_GRANTED
//                 ) {
//                     // TODO: Consider calling
//                     //    ActivityCompat#requestPermissions
//                     // here to request the missing permissions, and then overriding
//                     //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                     //                                          int[] grantResults)
//                     // to handle the case where the user grants the permission. See the documentation
//                     // for ActivityCompat#requestPermissions for more details.
//                     return
//                 }
//                 map.isMyLocationEnabled = true
//             } else {
//                 Toast.makeText(
//                     requireContext(),
//                     "Para activar la localización ve a ajustes y acepta los permisos",
//                     Toast.LENGTH_SHORT
//                 ).show()
//             }
//             else -> {
//             }
//         }
//     }
//
//
//     // private fun getLocation() {
//     //     Task<Location> loca
//
//         // if (ActivityCompat.checkSelfPermission(
//         //         requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
//         //     ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//         //         requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
//         //     ) != PackageManager.PERMISSION_GRANTED
//         // ) {
//         //     ActivityCompat.requestPermissions(
//         //         requireActivity(),
//         //         arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//         //         LOCATION_REQUEST_CODE
//         //     )
//         // } else {
//         //     val locationGPS: Location? =
//         //         locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//         //     if (locationGPS != null) {
//         //         val lat = locationGPS.latitude
//         //         val longi = locationGPS.longitude
//         //         latitude = lat.toString()
//         //         longitude = longi.toString()
//         //         Log.i(TAG, "Your Location: \nLatitude: $latitude\nLongitude: $longitude")
//         //     } else {
//         //         Log.i(TAG, "Unable to find location")
//         //     }
//         // }
//     // }
// }