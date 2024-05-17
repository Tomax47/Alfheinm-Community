package com.alfheim.aflheim_community.converter.payment;

import com.alfheim.aflheim_community.dto.publication.PublicationForm;
import com.alfheim.aflheim_community.dto.stripe.StripeChargeDto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStripeChargeDto implements Converter<String, StripeChargeDto> {
    @Override
    public StripeChargeDto convert(String data) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

        // Getting the data from the JsonObject
        return gson.fromJson(jsonObject, StripeChargeDto.class);
    }
}
