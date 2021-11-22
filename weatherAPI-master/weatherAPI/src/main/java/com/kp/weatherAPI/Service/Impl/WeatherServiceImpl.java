package com.kp.weatherAPI.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.weatherAPI.EntityDTO.*;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.Exceptions.ConflictException;
import com.kp.weatherAPI.Exceptions.NotFoundException;
import com.kp.weatherAPI.Repository.WeatherRepository;
import com.kp.weatherAPI.Service.WeatherService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static String GEOMETRY_NOT_FOUND = "Weather doesn't exists.";
    private final static String WEATHER_API_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=";
    private final static String USER_AGENT = "user-agent";
    private final static String LAT_LON_OUT_BOUNDS = "The Lat Lon configuration is out of bounds. Value of latitude  range from -90 to 90° and -180° to 180° for longitude";


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
        Future<List<Weather>> weatherList = updateAsync();
        if (!weatherList.isDone()) {
            log.info("Async failed.");
        } else {
            log.info("Saving the updated list. Scheduled ended.");
            weatherRepository.saveAll(weatherList.get());
        }
    }

    @Override
    @Async("autoWeatherUpdate")
    public Future<List<Weather>> updateAsync() {
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

    @Override
    public Weather getWeatherFromList(double lat, double lon, List<Weather> weatherList) {
        return weatherList.stream()
                .filter(weather -> weather.getGeometry().getCoordinates().get(1).equals(lat) && weather.getGeometry().getCoordinates().get(0).equals(lon))
                .findFirst().get();
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
    public Weather newWeatherOrder(Double lat, Double lon) {
        log.info("Waiting for weather to be fetched and passing it to weatherSave().");
        if (validateGeometryCompatibility(lat, lon)) {
            Weather weather = getNewWeatherOrder(lat, lon);
            weatherSave(weather);
            return weather;
        } else
            return null;
    }

    @Override
    public Boolean validateGeometryCompatibility(Double lat, Double lon) {
        if (lat <= 90 && lat >= -90 && lon <= 180 && lon >= -180) {
            return true;
        } else
            throw new ConflictException(LAT_LON_OUT_BOUNDS);
    }

    // TODO UNDER TESTING
    @Override
    public WeatherDTO getWeatherByGeometry(double lat, double lon) {
        log.info("Returning weather entity if exists");
        Weather weather = weatherRepository.findById(geometryService.validateIfGeometryByIdExists(
                        geometryService.getGeometryIdByLatLon(lat, lon)))
                .orElseThrow(() -> new NotFoundException(GEOMETRY_NOT_FOUND));
        return getWeatherForWeek(weather);
    }

    @Override
    public List<WeatherDTO> getWeatherListForIncomingDays() {
        return getWeatherList().stream().map(this::getWeatherForWeek).collect(Collectors.toList());
    }

    @Override
    public WeatherDTO getWeatherForWeek(Weather weather) {
        WeatherDTO weatherDAO = modelMapper.map(weather, WeatherDTO.class);
        Set<TimeseriesDTO> timeseriesDAOList = new HashSet<>();
        Set<String> daySet = new HashSet<>();
        daySet.add(String.valueOf(LocalDate.now()));
        daySet.add(String.valueOf(LocalDate.now().plusDays(1)));
        daySet.add(String.valueOf(LocalDate.now().plusDays(2)));
        daySet.add(String.valueOf(LocalDate.now().plusDays(3)));
        daySet.add(String.valueOf(LocalDate.now().plusDays(4)));
        daySet.add(String.valueOf(LocalDate.now().plusDays(5)));
        daySet.add(String.valueOf(LocalDate.now().plusDays(6)));

        List<Timeseries> timeseries = weather.getProperties().getTimeseries();
        timeseries.sort(Timeseries::compareTo);
        daySet.forEach(day -> timeseries
                .stream()
                .filter(timeseries1 -> timeseries1.getTime().substring(0, 10).equals(day))
                .map(timeseries1 -> {
                    DataDTO dataDAO = new DataDTO();
                    dataDAO.setTime(timeseries1.getTime().substring(11));
                    dataDAO.setInstant(new InstantDTO(
                            new DetailsDTO(
                                    timeseries1.getData().getInstant().getDetails().getAirPressureAtSeaLevel(),
                                    timeseries1.getData().getInstant().getDetails().getAirTemperature(),
                                    timeseries1.getData().getInstant().getDetails().getCloudAreaFraction(),
                                    timeseries1.getData().getInstant().getDetails().getRelativeHumidity(),
                                    timeseries1.getData().getInstant().getDetails().getWindFromDirection(),
                                    timeseries1.getData().getInstant().getDetails().getWindSpeed())));

                    TimeseriesDTO timeseriesDAO = new TimeseriesDTO();
                    timeseriesDAO.setDate(timeseries1.getTime().substring(0, 10));
                    timeseriesDAO.setData(dataDAO);
                    timeseriesDAOList.add(timeseriesDAO);
                    return timeseriesDAO;
                }).collect(Collectors.toSet()));

        List<TimeseriesDTO> time = new ArrayList<>(timeseriesDAOList);
        time.sort(TimeseriesDTO::compareTo);
        weatherDAO.getProperties().setTimeseries(time);
        return weatherDAO;
    }

    @Override
    public List<Weather> getWeatherList() {
        return weatherRepository.findAll();
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

}
