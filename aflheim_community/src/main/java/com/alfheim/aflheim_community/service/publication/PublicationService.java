package com.alfheim.aflheim_community.service.publication;

import com.alfheim.aflheim_community.dto.publication.PublicationDto;
import com.alfheim.aflheim_community.dto.publication.PublicationForm;

import java.util.List;
import java.util.Optional;

public interface PublicationService {

    PublicationDto getPublicationById(Long id);
    List<PublicationDto> searchProducts(Integer size, Integer page, String query, String sort, String direction);

    Long addPublication(PublicationForm publicationForm, String username);
    int deletePublication(Long id, String username);

    int changeUpVoteStatus(Long id, String username);
    int changeDownVoteStatus(Long id, String username);

}
