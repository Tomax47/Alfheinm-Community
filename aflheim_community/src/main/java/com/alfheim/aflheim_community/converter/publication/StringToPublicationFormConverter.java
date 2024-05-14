package com.alfheim.aflheim_community.converter.publication;

import com.alfheim.aflheim_community.dto.publication.PublicationForm;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPublicationFormConverter implements Converter<String, PublicationForm> {

    @Override
    public PublicationForm convert(String dataString) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(dataString, JsonObject.class);

        // Getting the data from the JsonObject
        return gson.fromJson(jsonObject, PublicationForm.class);
    }
}
