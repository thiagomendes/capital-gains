package capital.gains.infra.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

    private JsonParser() {
    }

    public static <T> T toObject(String json, Class<T> type) throws JsonProcessingException {
        return OBJECT_MAPPER.reader().forType(type).readValue(json);
    }

    public static <T> String toJson(T object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }
}
