package ru.sharipov.dataprocessor;

import ru.sharipov.model.Measurement;

import java.io.IOException;
import java.util.List;

public interface Loader {

    List<Measurement> load() throws IOException;
}
