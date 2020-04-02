package org.rightmove.property.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.rightmove.property.model.Properties;
import org.rightmove.property.exceptions.DataNotFoundException;
import org.rightmove.property.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;


@Slf4j
@Repository
@RequiredArgsConstructor
public class PropertyDataSetRestRepository implements PropertyDataSetRepository {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${property.dataset.url}")
    private String url;

    @Override
    public Properties find()  {
        log.info("find all properties from remote site {} ", url);

        Properties properties = Properties.builder().properties(Collections.emptyList()).build();

        try {
            ResponseEntity<String> propertyDataSet = restTemplate.getForEntity(url, String.class);
            String body = propertyDataSet.getBody();

            log.info(body);

            if (StringUtils.isNotBlank(body) &&
                    !StringUtils.containsOnly(body, "{properties:[]}") &&
                    !StringUtils.containsOnly(body, "{}")) {

                properties =  objectMapper.readValue(body, Properties.class);
                log.info("properties = {}", properties);
            }

        } catch (HttpClientErrorException e) {
            log.error("Undable to get property data", e );
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new DataNotFoundException(e);
            } else {
                throw new ServiceException(e);
            }
        } catch (HttpServerErrorException e) {
            log.error("Undable to get property data", e );
            throw new ServiceException(e);
        } catch (RuntimeException | IOException e) {
            log.error("Undable to get property data", e );
            throw new ServiceException((RuntimeException) e);
        }
        return properties;
    }
}
