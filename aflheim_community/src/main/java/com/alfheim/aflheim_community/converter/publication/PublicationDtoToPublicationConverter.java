package com.alfheim.aflheim_community.converter.publication;

import com.alfheim.aflheim_community.dto.publication.PublicationDto;
import com.alfheim.aflheim_community.model.publication.Publication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PublicationDtoToPublicationConverter implements Converter<PublicationDto, Publication> {

    @Override
    public Publication convert(PublicationDto publicationDto) {
        return null;
    }
}
