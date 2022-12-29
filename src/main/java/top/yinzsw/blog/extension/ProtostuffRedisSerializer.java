package top.yinzsw.blog.extension;

import io.netty.util.internal.EmptyArrays;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Protostuff Redis 序列化器
 *
 * @author yinzsW
 * @since 22/12/27
 **/
@Component
public class ProtostuffRedisSerializer implements RedisSerializer<Object> {
    private static final Schema<ObjectWrapper> SCHEMA = RuntimeSchema.getSchema(ObjectWrapper.class);
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public byte[] serialize(Object source) throws SerializationException {
        if (Objects.isNull(source)) {
            return EmptyArrays.EMPTY_BYTES;
        }

        try {
            return ProtostuffIOUtil.toByteArray(new ObjectWrapper(source), SCHEMA, BUFFER);
        } finally {
            BUFFER.clear();
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        var objectWrapper = new ObjectWrapper();
        ProtostuffIOUtil.mergeFrom(bytes, objectWrapper, SCHEMA);
        return objectWrapper.getObject();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObjectWrapper {
        private Object object;
    }
}