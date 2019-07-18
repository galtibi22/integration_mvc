package smartspace.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Ignore;
import org.springframework.boot.json.JsonParseException;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import smartspace.CommonTest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@Ignore
public class ControllerCommonTest extends CommonTest {

    protected WebApplicationContext webApplicationContext;

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    protected void mvcResultIsOk(MvcResult mvcResult){
        int status = mvcResult.getResponse().getStatus();
        assertEquals("The code response should be 200",200, status);
    }
}
