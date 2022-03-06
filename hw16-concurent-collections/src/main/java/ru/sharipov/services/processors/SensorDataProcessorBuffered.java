package ru.sharipov.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sharipov.api.SensorDataProcessor;
import ru.sharipov.api.model.SensorData;
import ru.sharipov.lib.SensorDataBufferedWriter;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final Set<SensorData> bufferedData;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        bufferedData = new TreeSet<>(Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {
        if (bufferedData.size() >= bufferSize) {
            flush();
        }
        bufferedData.add(data);
    }

    public void flush() {
        try {
            synchronized (this) {
                if (!bufferedData.isEmpty()) {
                    writer.writeBufferedData(List.copyOf(bufferedData));
                    bufferedData.clear();
                }
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
