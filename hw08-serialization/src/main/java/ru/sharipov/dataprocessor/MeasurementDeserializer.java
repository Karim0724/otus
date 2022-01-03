package ru.sharipov.dataprocessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.sharipov.model.Measurement;

import java.io.IOException;

public class MeasurementDeserializer extends StdDeserializer<Measurement> {
    protected MeasurementDeserializer(Class<?> vc) {
        super(vc);
    }

    protected MeasurementDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected MeasurementDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public Measurement deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String name = node.get("name").asText();
        double value = node.get("value").asDouble();

        return new Measurement(name, value);
    }
}
