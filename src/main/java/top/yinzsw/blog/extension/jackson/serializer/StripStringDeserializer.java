package top.yinzsw.blog.extension.jackson.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Objects;

@JsonComponent
public class StripStringDeserializer extends StringDeserializer {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        String value = super.deserialize(jsonParser, context);
        return Objects.isNull(value) ? null : value.strip();
    }
}