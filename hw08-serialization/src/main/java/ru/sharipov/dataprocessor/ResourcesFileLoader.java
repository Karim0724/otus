package ru.sharipov.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.sharipov.model.Measurement;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final ObjectMapper objectMapper;
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Measurement.class, new MeasurementDeserializer(Measurement.class));
        objectMapper.registerModule(simpleModule);
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            return objectMapper.readValue(is, new TypeReference<>() {});
        } catch (IOException e) {
            throw new FileProcessException("File with path: " + fileName + " not appropriate", e);
        }
    }
}
