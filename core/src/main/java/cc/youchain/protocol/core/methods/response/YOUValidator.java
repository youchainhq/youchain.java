package cc.youchain.protocol.core.methods.response;

import cc.youchain.protocol.ObjectMapperFactory;
import cc.youchain.protocol.core.Response;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

/**
 * Validator object returned by:
 * <ul>
 * <li>you_validatorByMainAddress</li>
 * </ul>
 */
public class YOUValidator extends Response<Validator> {
//
    @Override
    @JsonDeserialize(using = YOUValidator.ResponseDeserialiser.class)
    public void setResult(Validator result) {
        super.setResult(result);
    }

    public Validator getValidator() {
        return this.getResult();
    }
//
    public static class ResponseDeserialiser extends JsonDeserializer<Validator> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public Validator deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, Validator.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }

}