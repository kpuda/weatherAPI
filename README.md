# weatherAPI
Database used - MySql.<br><br>
Postman package collection and JSON test files in folder above.


# Available endpoints:

# <b>-GET:</b><br>
/new<br> eg. /new?lat=50&lon=19 - imports weather info based from provided Latitude and Longtitude,<br><br>
/all - extracts all data available to list,<br><br>
/byGeo<br> eg. byGeo?lat=50&lon=19 - extracts data based from provided Latitude and Longtitude.<br><br>
# <b>-POST<br>
/saveWeather<br>  @RequestBody Weather provided in json format.<br><br>
# <b>-PUT</b><br>
/update<br> updates weather info for every location stored in database.<br><br>
# <b>-DELETE</b><br>
/deleteLocationByGeo<br> eg. /deleteLocationByGeo?lat=30&lon=1 - deletes everything associated with this location from database.<br>
