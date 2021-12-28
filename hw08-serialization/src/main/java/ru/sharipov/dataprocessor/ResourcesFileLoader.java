package ru.sharipov.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.sharipov.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {
    private final ObjectMapper objectMapper;
    private final File srcFile;

    public ResourcesFileLoader(String fileName) {
        this.objectMapper = new ObjectMapper();
        this.srcFile = createFile(fileName);
        validateFile();
    }

    private File createFile(String fileName) {
        try {
            return new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource(fileName)).toURI());
        } catch (URISyntaxException e) {
            throw new FileProcessException("Can't get resource by name: " + fileName, e);
        }
    }

    private void validateFile() {
        String filePath = srcFile.getAbsolutePath();
        if (!srcFile.exists()) {
            throw new FileProcessException("File: " + filePath + " doesn't exist");
        }
        if (!srcFile.isFile()) {
            throw new FileProcessException("File: " + filePath + " is not a file");
        }
        if (!srcFile.canRead()) {
            throw new FileProcessException("File: " + filePath + " can't be read");
        }
    }

    @Override
    public List<Measurement> load() {
        try {
            return objectMapper.readValue(srcFile, new TypeReference<>() {} );
        } catch (IOException e) {
            throw new FileProcessException("File with path: " + srcFile.getAbsolutePath() + " not appropriate", e);
        }
    }
}
