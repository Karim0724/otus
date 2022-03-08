package ru.sharipov.api;

import ru.sharipov.api.model.SensorData;

public interface SensorsDataServer {
    void onReceive(SensorData sensorData);
}
