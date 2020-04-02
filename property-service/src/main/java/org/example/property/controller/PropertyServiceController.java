package org.example.property.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.property.controller.model.PostCodeAveragePrice;
import org.example.property.controller.model.PropertyType;
import org.example.property.service.PropertyService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/properties")
@Slf4j
@RequiredArgsConstructor
public class PropertyServiceController {

    @Qualifier("PropertyServiceImpl")
    private final PropertyService propertyService;
    private final PropertyValidation propertyValidation;

    @GetMapping(path = "/averageprice/{postcode}")
    @ResponseStatus(HttpStatus.OK)
    public PostCodeAveragePrice calculateAveragePriceAroundPostCode(@PathVariable("postcode") String aroundPostCode) {
        log.info("postcode = {}", aroundPostCode);

        propertyValidation.validatePostCode(aroundPostCode);

        var averagePrice = propertyService.calculateAveragePriceAroundPostCode(aroundPostCode);
        var result = PostCodeAveragePrice.builder().postcode(aroundPostCode).averagePrice(averagePrice).build();
        log.info("postCodeAveragePrice = {}", result);
        return result;
    }

    @GetMapping(path = "/price/difference/{fromType}/{toType}")
    @ResponseStatus(HttpStatus.OK)
    public Double calculateAveragePriceBetweenTypes(@PathVariable("fromType") String fromType,
                                                                  @PathVariable("toType") String toType) {
        log.info("differences between {} and {}", fromType, toType);

        propertyValidation.validateType(fromType);
        propertyValidation.validateType(toType);

        var difference = propertyService.calculateDifferenceInAveragePriceForTypes(
                PropertyType.valueOf(fromType), PropertyType.valueOf(toType));
        log.info("difference = {}", difference);
        return difference;
    }
}
