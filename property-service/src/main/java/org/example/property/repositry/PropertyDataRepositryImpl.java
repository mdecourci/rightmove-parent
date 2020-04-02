package org.example.property.repositry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.property.exceptions.DataNotFoundException;
import org.example.property.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Repository
public class PropertyDataRepositryImpl implements PropertyDataRepositry {
    private final RestTemplate  restTemplate;

    @Value("${property.endpoint.url}")
    private String url;

    @Override
    public Set<Property> findProperties() {
        log.info("find all properties from remote site {} ", url);

        try {
            ResponseEntity<Properties> propertyDataSet = restTemplate.getForEntity(url, Properties.class);
            var foundProperties = new HashSet<Property>();

            if (propertyDataSet.getBody() != null && propertyDataSet.getBody().getProperties() != null) {
                foundProperties = new HashSet<Property>(propertyDataSet.getBody().getProperties());
            }

            log.info("found properties = {}", foundProperties);

            return foundProperties;

        } catch (HttpClientErrorException e) {
            log.error("HTTP client error unable to get property data", e );
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new DataNotFoundException("HTTP client error unable to get property data", e);
            } else {
                throw new ServiceException("HTTP client error unable to get property data", e);
            }
        } catch (HttpServerErrorException e) {
            log.error("HTTP server error unable to get property data", e );
            throw new ServiceException("HTTP server error unable to get property data", e);
        } catch (RuntimeException e) {
            log.error("Unknown error getting data", e);
            throw new ServiceException("Unknown error getting data", e);
        }
    }
}
