package ru.sharipov;

import ru.sharipov.dataprocessor.FileSerializer;
import ru.sharipov.dataprocessor.ProcessorAggregator;
import ru.sharipov.dataprocessor.ResourcesFileLoader;

import java.util.Map;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ResourcesFileLoader fileLoader = new ResourcesFileLoader("inputData.json");
        ProcessorAggregator aggregator = new ProcessorAggregator();
        FileSerializer fileSerializer = new FileSerializer("outputData.json");

        Map<String, Double> aggregatedData = aggregator.process(fileLoader.load());
        fileSerializer.serialize(aggregatedData);
    }
}
