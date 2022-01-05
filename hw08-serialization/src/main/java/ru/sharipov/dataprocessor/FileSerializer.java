package ru.sharipov.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final File destFile;
    private final ObjectMapper objectMapper;

    public FileSerializer(String fileName) {
        objectMapper = new ObjectMapper();
        this.destFile = new File(fileName);
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try {
            objectMapper.writeValue(destFile, data);
        } catch (IOException e) {
            throw new FileProcessException("Not appropriate file" + destFile.getAbsolutePath());
        }
    }
}
