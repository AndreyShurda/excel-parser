package test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;

public class JsonToXml {

    public static void main(String[] args) throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        JsonNode jsonNode = mapper.readTree("{\n" +
                "  \"FlowGenerator\":{\n" +
                "    \"IdentificationProfiles\":{\n" +
                "      \"Product\":{\n" +
                "        \"UniversalIdentifier\":\"c5442aff-2c9e-3a21-adac-f1700e373bdb\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"MeasurementProfilesCollection\":[{\n" +
                "      \"AcousticSignature\":{\n" +
                "        \"ProcessingType\":-1556905238,\n" +
                "        \"StartDateTime\":\"2019-11-06T14:54:34.123Z\",\n" +
                "        \"EndDateTime\":\"2019-11-06T14:51:43.841Z\",\n" +
                "        \"AcousticCepstrum\":[-0.0009008, -0.001663, -0.000691, -0.1009008, 0.101663, -0.000691, -0.2009008, 0.701663, -0.000691, -0.000795,\n" +
                "          -0.0009008, -0.001663, -0.000691, -0.1009008, 0.101663, -0.000691, -0.2009008, 0.701663, -0.000691, -0.000795,\n" +
                "          -0.0009008, -0.001663, -0.000691, -0.1009008, 0.101663, -0.000691, -0.2009008, 0.701663, -0.000691, -0.000795,\n" +
                "          -0.0009008, -0.001663, -0.000691, -0.1009008, 0.101663, -0.000691, -0.2009008, 0.701663, -0.000691, -0.000795,\n" +
                "          -0.0009008, -0.001663, -0.000691, -0.1009008, 0.101663, -0.000691, -0.2009008, 0.701663, -0.000691, -0.000795,\n" +
                "          -0.0009008, -0.001663, -0.000691, -0.1009008, 0.101663, -0.000691, -0.2009008, 0.701663, -0.000691, -0.000795,\n" +
                "          -0.0009008, -0.001663, -0.000691, -0.1009008, 0.101663, -0.000691, -0.2009008, 0.701663, -0.000691, -0.000795,\n" +
                "          -0.0009008, -0.001663, -0.000691, -0.1009008, 0.101663, -0.000691, -0.2009008, 0.701663, -0.000691, -0.000795,\n" +
                "          -0.0009008, -0.001663, -0.000691, -0.1009008, 0.101663, -0.000691]\n" +
                "      }\n" +
                "    }]\n" +
                "  }\n" +
                "}");


        String x = XML.toString(new JSONObject(jsonNode.toString()));
        System.out.println(x);

    }
}
