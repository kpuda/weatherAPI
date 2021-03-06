package com.kp.weatherAPI.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kp.weatherAPI.Entity.Timeseries;
import com.kp.weatherAPI.Entity.Weather;
import com.kp.weatherAPI.EntityDTO.*;
import com.kp.weatherAPI.Exceptions.NotFoundException;
import com.kp.weatherAPI.Exceptions.ValuesOutOfBoundsException;
import com.kp.weatherAPI.Repository.WeatherRepository;
import com.kp.weatherAPI.Service.GeometryService;
import com.kp.weatherAPI.Service.TimeseriesService;
import com.kp.weatherAPI.Service.WeatherService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;
    private final TimeseriesService timeseriesService;
    private final GeometryService geometryService;
    private final ObjectMapper objectMapper;
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
    @Async("autoWeatherUpdate")
    public HttpStatus updateAll() {
        log.info("Started scheduled updateAll()");
        updateWeatherAsync();
        return HttpStatus.ACCEPTED;
    }

    @Override
    public void updateWeatherAsync() throws InterruptedException {
        log.info("added thread.sleep to check if async works. IT WORKS!");
        Thread.sleep(8000);
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
        weatherRepository.saveAll(refreshedWeatherList);
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

        setAvgTemperatureForList(daySet, timeseriesDTO, timeseries);

        weatherDTO.getProperties().setTimeseries(timeseriesDTO);
        return weatherDTO;
    }

    public List<TimeseriesDTO> setAvgTemperatureForList(Set<String> daySet, List<TimeseriesDTO> timeseriesDTO, List<Timeseries> timeseries) {
        Map<String, List<String>> avgTemp = new TreeMap<>();

        for (String day : daySet) {
            OptionalDouble averageMorning = timeseries
                    .stream()
                    .filter(timeseries1 -> timeseries1.getTime().substring(0, 10).equals(day))
                    .filter(timeseriesObject -> Integer.parseInt(timeseriesObject.getTime().substring(11, 13)) >= 1 && Integer.parseInt(timeseriesObject.getTime().substring(11, 13)) <= 6)
                    .mapToDouble(timeseriesObject -> timeseriesObject.getData().getInstant().getDetails().getAirTemperature()).average();
            OptionalDouble averageNoon = timeseries
                    .stream()
                    .filter(timeseries1 -> timeseries1.getTime().substring(0, 10).equals(day))
                    .filter(timeseriesObject -> Integer.parseInt(timeseriesObject.getTime().substring(11, 13)) >= 7 && Integer.parseInt(timeseriesObject.getTime().substring(11, 13)) <= 12)
                    .mapToDouble(timeseriesObject -> timeseriesObject.getData().getInstant().getDetails().getAirTemperature()).average();
            OptionalDouble averageAfterNoon = timeseries
                    .stream()
                    .filter(timeseries1 -> timeseries1.getTime().substring(0, 10).equals(day))
                    .filter(timeseriesObject -> Integer.parseInt(timeseriesObject.getTime().substring(11, 13)) >= 13 && Integer.parseInt(timeseriesObject.getTime().substring(11, 13)) <= 18)
                    .mapToDouble(timeseriesObject -> timeseriesObject.getData().getInstant().getDetails().getAirTemperature()).average();
            OptionalDouble averageEvening = timeseries
                    .stream()
                    .filter(timeseries1 -> timeseries1.getTime().substring(0, 10).equals(day))
                    .filter(timeseriesObject -> Integer.parseInt(timeseriesObject.getTime().substring(11, 13)) >= 19 || Integer.parseInt(timeseriesObject.getTime().substring(11, 13)) == 0)
                    .mapToDouble(timeseriesObject -> timeseriesObject.getData().getInstant().getDetails().getAirTemperature()).average();

            BigDecimal bigAfterNoon = BigDecimal.valueOf(averageAfterNoon.getAsDouble()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bigNoon = BigDecimal.valueOf(averageNoon.getAsDouble()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bigEvening = BigDecimal.valueOf(averageEvening.getAsDouble()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bigMorning = BigDecimal.valueOf(averageMorning.getAsDouble()).setScale(2, RoundingMode.HALF_UP);

            avgTemp.put(day, List.of(
                    String.valueOf(bigMorning),
                    String.valueOf(bigNoon),
                    String.valueOf(bigAfterNoon),
                    String.valueOf(bigEvening)
            ));
        }
        for (TimeseriesDTO time : timeseriesDTO) {
            if (avgTemp.containsKey(time.getDate())) {
                time.setAvgTemperatureMorning(Double.parseDouble(avgTemp.get(time.getDate()).get(0)));
                time.setAvgTemperatureNoon(Double.parseDouble(avgTemp.get(time.getDate()).get(1)));
                time.setAvgTemperatureAfterNoon(Double.parseDouble(avgTemp.get(time.getDate()).get(2)));
                time.setAvgTemperatureEvening(Double.parseDouble(avgTemp.get(time.getDate()).get(3)));
            }
        }
        return timeseriesDTO;
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
        Set<String> incomingDays = new TreeSet<>();
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
}
