package ru.sharipov.dataprocessor;

import ru.sharipov.model.Measurement;

import java.util.List;
import java.util.Map;

public interface Processor {

    Map<String, Double> process(List<Measurement> data);
}
