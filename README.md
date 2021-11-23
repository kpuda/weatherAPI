# weatherAPI - not finished yet
This web service focuses on consuming an external API to the data which is then saved to the database. You are able to store a forecast for given location within few seconds. The forecast for stored locations is sending request to update every 30 minutes. 
<h3>Technologies used:</h3><p>
- Spring framework <br>
- MySQL database <br>
- Swagger - for API visualisation<br><br>
  
 <h5>Swagger link: http://localhost:8080/swagger-ui.html#/</h5>


# Available endpoints: 

# <b>-GET:</b><br>

<b>/weather/list</b><br> Extracting forecasts for every location stored in database.<br><br>
# <b>-POST<br>
<b>/weather/new   &nbsp;  eg. /weather/new?lat=50&lon=19 </b><br> Fetching forecast for given latitude and longitude, and then saving it to the database.<br><br>
# <b>-PUT</b><br>
<b>/weather/update</b><br> Updating weather forecast for every location stored in database.<br><br>
# <b>-DELETE</b><br>
<b>/weather/delete eg. /weather/delete?lat=30&lon=1</b> <br>Deleting everything associated with given location from database.<br>
