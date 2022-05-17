package com.example.lambdawithapigateway.functions;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.lambdawithapigateway.dto.SampleRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

@Component
@Slf4j
public class DemoLambda implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @SneakyThrows
    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent apiGatewayRequest) {
        int status = 200;
        HashMap<String, String> response = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            SampleRequest sampleRequest =
                mapper.readValue(apiGatewayRequest.getBody(), SampleRequest.class);
            response.put("message", "Request received with name: " + sampleRequest.getName());
        } catch (Exception e) {
            status = 500;
            response.put("message", "Some exception occurred");
            log.error(e.getMessage());
        }
        return new APIGatewayProxyResponseEvent().withIsBase64Encoded(false).withStatusCode(status)
            .withBody(mapper.writeValueAsString(response))
            .withHeaders(Collections.singletonMap("Content-Type", "application/json"));
    }
}
