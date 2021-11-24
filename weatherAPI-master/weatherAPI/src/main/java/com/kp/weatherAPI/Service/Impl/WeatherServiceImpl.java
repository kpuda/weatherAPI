package com.kp.weatherAPI.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.weatherAPI.EntityDTO.*;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Exceptions.NotFoundException;
import com.kp.weatherAPI.Exceptions.ValuesOutOfBoundsException;
import com.kp.weatherAPI.Repository.WeatherRepository;
import com.kp.weatherAPI.Service.WeatherService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor

public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final TimeseriesServiceImpl timeseriesService;
    private final GeometryServiceImpl geometryService;
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static String GEOMETRY_NOT_FOUND = "Weather doesn't exists.";
    private final static String WEATHER_API_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=";
    private final static String USER_AGENT = "user-agent";
    private final static String LAT_LON_OUT_BOUNDS = "The Lat Lon configuration is out of bounds. Value of latitude  range from -90 to 90° and -180° to 180° for longitude";
    private final static String DB_CONNECTION_PROBLEM = "Problem connecting with data source";

    @Transactional
    @Override
    public void weatherSave(Weather weather) {
        if (geometryService.validateIfGeometryByIdDoesntExists(
                geometryService.getGeometryIdByLatLon(
                        weather.getGeometry().getCoordinates().get(1), weather.getGeometry().getCoordinates().get(0)))) {
            weatherRepository.save(weather);
        }
    }

    @SneakyThrows
    @Transactional
    @Override
    @Scheduled(fixedRateString = "PT30M")
    public void updateAll() {
        log.info("Started scheduled updateAll()");
        Future<List<Weather>> weatherList = updateWeatherAsync();
        if (!weatherList.isDone()) {
            log.info("Async failed.");
        } else {
            log.info("Saving the updated list. Scheduled ended.");
            weatherRepository.saveAll(weatherList.get());
        }
    }

    @Override
    @Async("autoWeatherUpdate")
    public Future<List<Weather>> updateWeatherAsync() {
        List<Weather> weatherList = getWeatherList();
        log.info("Getting weather list.");
        List<Weather> refreshedWeatherList = new ArrayList<>();
        weatherList.forEach(weather -> {
            Weather refreshedWeatherData = getNewWeatherOrder(weather.getGeometry().getCoordinates().get(1), weather.getGeometry().getCoordinates().get(0));
            Weather oldWeatherData = (
                    getWeatherFromList(
                            weather.getGeometry().getCoordinates().get(1),
                            weather.getGeometry().getCoordinates().get(0),
                            weatherList));

            refreshedWeatherList.add(
                    compareTimeseriesData(refreshedWeatherData, oldWeatherData)
            );
        });
        return AsyncResult.forValue(refreshedWeatherList);
    }

    @SneakyThrows
    @Override
    public Weather getNewWeatherOrder(Double lat, Double lon) {
        String url = WEATHER_API_URL + lat + "&lon=" + lon;
        HttpResponse<String> weatherResponse = Unirest.post(url).header("Content-type", USER_AGENT).asString();
        Weather weather = objectMapper.readValue(weatherResponse.getBody(), Weather.class);
        List<Timeseries> timeseries = weather.getProperties().getTimeseries();
        timeseries.sort(Timeseries::compareTo);
        weather.getProperties().setTimeseries(timeseries);
        return weather;
    }

    @Override
    public WeatherDTO newWeatherOrder(Double lat, Double lon) {
        log.info("Waiting for weather to be fetched and passing it to weatherSave().");
        if (validateGeometryCompatibility(lat, lon)) {
            Weather weather = getNewWeatherOrder(lat, lon);
            weatherSave(weather);
            return getWeatherForWeek(weather);
        } else
            return null;
    }

    @Override
    public WeatherDTO getWeatherByGeometry(double lat, double lon) {
        log.info("Returning weather entity if exists");
        Weather weather = weatherRepository.findById(geometryService.validateIfGeometryByIdExists(
                        geometryService.getGeometryIdByLatLon(lat, lon)))
                .orElseThrow(() -> new NotFoundException(GEOMETRY_NOT_FOUND));
        return getWeatherForWeek(weather);
    }

    @Override
    public WeatherDTO getWeatherForWeek(Weather weather) {
        WeatherDTO weatherDTO = convertToDTO(weather);
        Set<TimeseriesDTO> timeseriesDTOSet = new HashSet<>();
        Set<String> daySet = getIncomingDates();
        log.info("Preparing weather for {}, {} for next 7 days", weather.getGeometry().getCoordinates().get(1), weather.getGeometry().getCoordinates().get(0));

        List<Timeseries> timeseries = weather.getProperties().getTimeseries();
        daySet.forEach(day -> {
            List<DataDTO> dataDTOList = timeseries
                    .stream()
                    .filter(timeseriesObject -> timeseriesObject.getTime().substring(0, 10).equals(day))
                    .map(timeseriesObject -> {
                        DataDTO dataDTO = new DataDTO();
                        dataDTO.setTime(timeseriesObject.getTime().substring(11));
                        dataDTO.setDetails(new DetailsDTO(
                                timeseriesObject.getData().getInstant().getDetails().getAirPressureAtSeaLevel(),
                                timeseriesObject.getData().getInstant().getDetails().getAirTemperature(),
                                timeseriesObject.getData().getInstant().getDetails().getCloudAreaFraction(),
                                timeseriesObject.getData().getInstant().getDetails().getRelativeHumidity(),
                                timeseriesObject.getData().getInstant().getDetails().getWindFromDirection(),
                                timeseriesObject.getData().getInstant().getDetails().getWindSpeed()
                        ));
                        return dataDTO;
                    }).collect(Collectors.toList());
            timeseriesDTOSet.add(new TimeseriesDTO(day, dataDTOList));
        });
        List<TimeseriesDTO> timeseriesDTO = new ArrayList<>(timeseriesDTOSet);
        timeseriesDTO.sort(TimeseriesDTO::compareTo);
        weatherDTO.getProperties().setTimeseries(timeseriesDTO);
        return weatherDTO;
    }


    @Override
    public Weather compareTimeseriesData(Weather refreshedWeatherData, Weather oldWeatherData) {
        log.info("Updating forecast for lat: {}, lon: {}", refreshedWeatherData.getGeometry().getCoordinates().get(1), refreshedWeatherData.getGeometry().getCoordinates().get(0));
        List<Timeseries> newTimeseriesList = timeseriesService.updateWeatherTimeseries(
                oldWeatherData.getProperties().getTimeseries(), refreshedWeatherData.getProperties().getTimeseries());
        newTimeseriesList.sort(Timeseries::compareTo);
        oldWeatherData.getProperties().setTimeseries(newTimeseriesList);
        return oldWeatherData;
    }

    @Override
    public void deleteWeatherByGeometry(Double lat, Double lon) {
        log.info("Deleting weather for given location");
        weatherRepository.deleteById(
                geometryService.validateIfGeometryByIdExists(
                        geometryService.getGeometryIdByLatLon(lat, lon))
        );
    }

    @Override
    public List<Weather> getWeatherList() {
        return Optional.of(weatherRepository.findAll()).orElseThrow(() -> new NotFoundException(DB_CONNECTION_PROBLEM));
    }

    @Override
    public Weather getWeatherFromList(double lat, double lon, List<Weather> weatherList) {
        return weatherList.stream()
                .filter(weather -> weather.getGeometry().getCoordinates().get(1).equals(lat) && weather.getGeometry().getCoordinates().get(0).equals(lon))
                .findFirst().get();
    }

    @Override
    public List<WeatherDTO> getWeatherListForIncomingDays() {
        return getWeatherList().stream().map(this::getWeatherForWeek).collect(Collectors.toList());
    }

    @Override
    public Set<String> getIncomingDates() {
        Set<String> incomingDays = new HashSet<>();
        incomingDays.add(String.valueOf(LocalDate.now()));
        incomingDays.add(String.valueOf(LocalDate.now().plusDays(1)));
        incomingDays.add(String.valueOf(LocalDate.now().plusDays(2)));
        incomingDays.add(String.valueOf(LocalDate.now().plusDays(3)));
        incomingDays.add(String.valueOf(LocalDate.now().plusDays(4)));
        incomingDays.add(String.valueOf(LocalDate.now().plusDays(5)));
        incomingDays.add(String.valueOf(LocalDate.now().plusDays(6)));
        return incomingDays;
    }

    @Override
    public Boolean validateGeometryCompatibility(Double lat, Double lon) {
        if (lat <= 90 && lat >= -90 && lon <= 180 && lon >= -180) {
            return true;
        } else
            throw new ValuesOutOfBoundsException(LAT_LON_OUT_BOUNDS);
    }

    private WeatherDTO convertToDTO(Weather weather) {
        return new WeatherDTO(new GeometryDTO(
                weather.getGeometry().getCoordinates().get(1),
                weather.getGeometry().getCoordinates().get(0),
                weather.getGeometry().getCoordinates().get(2)),
                new PropertiesDTO());
    }

    //TODO under testing
    public void webScrape(double lat, double lon) throws UnirestException {
        String url = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" + lat + "&lon=" + lon;
        HttpResponse<String> httpResponse = Unirest.post(url).header("Content-type", USER_AGENT).asString();
        String word[] = httpResponse.getBody().split(" ");
        String city = word[11].substring(0, word[11].length() - 1);
        int words = httpResponse.getBody().compareTo("city");
        char c = httpResponse.getBody().charAt(words);
        System.out.println(words + ", " + c);
        //String voivodeship = word[13].substring(0, word[13].length() - 1);
        List<String> scrapedData = new ArrayList<>();
        scrapedData.add(city);
        // scrapedData.add(voivodeship);

    }
}
