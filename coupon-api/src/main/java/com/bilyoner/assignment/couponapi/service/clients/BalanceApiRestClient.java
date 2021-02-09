package com.bilyoner.assignment.couponapi.service.clients;

import com.bilyoner.assignment.couponapi.exception.CouponApiException;
import com.bilyoner.assignment.couponapi.exception.ErrorCodeEnum;
import com.bilyoner.assignment.couponapi.model.UpdateBalanceRequest;
import com.bilyoner.assignment.couponapi.model.UserBalanceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@NoArgsConstructor
public class BalanceApiRestClient {

    private static final String BASE_URL = "http://localhost:9090/v1";

    private static final String GET_USER_AMOUNT_URL = BASE_URL + "/balances/{userId}";

    private static final String UPDATE_BALANCE_URL = BASE_URL + "/balances";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void updateBalance(UpdateBalanceRequest updateBalanceRequest) {
        log.info("BalanceRestClient updateBalance start");
        try {
            String requestBody = objectMapper.writeValueAsString(updateBalanceRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.valueOf("application/json;charset=UTF-8"));

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Object> objectResponse = requestToClient(UPDATE_BALANCE_URL, HttpMethod.PUT, request);

            if (objectResponse.getStatusCode() != HttpStatus.OK) {
                throw new CouponApiException(ErrorCodeEnum.INTERNAL_SERVER_ERROR);
            }
            else {
                log.info("update balance ok");
            }
        }
        catch (Exception e) {
            log.error("updateBalance error ", e);
            throw new CouponApiException(ErrorCodeEnum.INTERNAL_SERVER_ERROR);
        }
    }

    public UserBalanceDto getUserAmount(Long userId) {
        log.info("BalanceRestClient getUserAmount start");
        try {
            Map<String, String> params = new HashMap<>();
            params.put("userId", String.valueOf(userId));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.valueOf("application/json;charset=UTF-8"));

            HttpEntity<String> request = new HttpEntity<>(null, headers);
            ResponseEntity<Object> objectResponse = getRequest(params, request);

            if (objectResponse.getStatusCode() != HttpStatus.OK) {
                throw new CouponApiException(ErrorCodeEnum.INTERNAL_SERVER_ERROR);
            }
            else {
                log.info("BalanceApiRestClient  ok");
                return objectMapper.convertValue(objectResponse.getBody(), UserBalanceDto.class);
            }
        }
        catch (Exception e) {
            log.error("getUserAmount ", e);
            throw new CouponApiException(ErrorCodeEnum.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Object> getRequest(Map<String, String> params, HttpEntity<String> request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(BalanceApiRestClient.GET_USER_AMOUNT_URL);
        String url = builder.buildAndExpand(params).toUri().toString();
        return requestToClient(url, HttpMethod.GET, request);
    }

    private ResponseEntity<Object> requestToClient(String requestUrl, HttpMethod requestMethod, HttpEntity<String> request) {
        log.debug("doRequest started. url:[{}]", requestUrl);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(requestUrl, requestMethod, request, Object.class);
    }

}
