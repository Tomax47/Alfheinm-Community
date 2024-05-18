package com.alfheim.aflheim_community.service.publication;

import com.alfheim.aflheim_community.converter.publication.PublicationToPublicationDtoConverter;
import com.alfheim.aflheim_community.dto.publication.PublicationDto;
import com.alfheim.aflheim_community.dto.publication.PublicationForm;
import com.alfheim.aflheim_community.exception.publication.PublicationNotFoundException;
import com.alfheim.aflheim_community.exception.publication.PublicationPageNotFoundException;
import com.alfheim.aflheim_community.exception.server.BadRequestException;
import com.alfheim.aflheim_community.exception.server.InternalServerErrorException;
import com.alfheim.aflheim_community.exception.user.UserNotFoundException;
import com.alfheim.aflheim_community.exception.user.UserUnauthorizedRequestException;
import com.alfheim.aflheim_community.model.publication.Publication;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.repository.PublicationRepo;
import com.alfheim.aflheim_community.repository.UserRepo;
import com.alfheim.aflheim_community.service.file.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    private PublicationToPublicationDtoConverter publicationToPublicationDtoConverter;

    @Autowired
    private PublicationRepo publicationRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public PublicationDto getPublicationById(Long id) {

        Optional<Publication> publicationOptional = publicationRepo.findById(id);

        if (publicationOptional.isPresent()) {
            PublicationDto publicationDto = publicationToPublicationDtoConverter.convert(publicationOptional.get());

            return publicationDto;
        }

        // Publication not found
        log.error("Publication not found (PublicationServiceImpl.getPublicationById)");
        throw new PublicationPageNotFoundException("Publication not found");
    }

    @Override
    public Long addPublication(PublicationForm publicationForm, String username) {

        Optional<User> userOptional = userRepo.findByUsername(username);

        if (userOptional.isPresent()) {

            // Building the object
            Publication publication = Publication.builder()
                    .author(userOptional.get())
                    .title(publicationForm.getTitle())
                    .description(publicationForm.getDescription())
                    .content(publicationForm.getContent())
                    .categories(publicationForm.getCategory())
                    .build();

            // Saving image
            if (isAcceptableImageFormat(publicationForm.getCoverImage())) {
                // Valid image content type
                String imageStorageName = fileStorageService.saveFile(publicationForm.getCoverImage());

                // Setting the image in the publication object
                publication.setCoverImage(fileStorageService.findByStorageName(imageStorageName));

                // Saving
                try {
                    Publication savedPublication = publicationRepo.save(publication);

                    // Success
                    return savedPublication.getId();

                } catch (Exception e) {
                    // Error 500
                    log.error("Internal error (PublicationServiceImpl.addPublication)");
                    throw new InternalServerErrorException("Something went seriously wrong!");
                }
            }

            // Bad request. Invalid image type
            log.error("Bad request (PublicationServiceImpl.addPublication)");
            throw new BadRequestException("You have made a bad request");
        }

        // User not found
        log.error("User not found (PublicationServiceImpl.addPublication)");
        throw new UserNotFoundException("User not found");
    }

    @Override
    public int deletePublication(Long publicationId, String username) {

        Optional<Publication> publicationOptional = publicationRepo.findById(publicationId);
        Optional<User> userOptional = userRepo.findByUsername(username);

        if (publicationOptional.isPresent()) {
            // Publication found
            if (userOptional.isPresent()) {
                // User exist
                Publication publication = publicationOptional.get();
                if (publication.getAuthor().getUsername().equals(username) || userOptional.get().getRole().equals("ADMIN")) {
                    // Authorized. deleting publication...

                    System.out.println("111111111111111");
                    List<User> upVotesList = publication.getUpVotes();
                    List<User> downVotesList = publication.getDownVotes();

                    upVotesList.clear();
                    downVotesList.clear();

                    System.out.println("\n\nLIST UP VOTES ");
                    publication.setDownVotes(downVotesList);
                    publication.setUpVotes(upVotesList);

                    Publication saved = publicationRepo.save(publication);
                    System.out.println("222222222222222222222");

                    List<Publication> createdPublications = userOptional.get().getCreatedPublications();
                    createdPublications.remove(saved);
                    userRepo.save(userOptional.get());

                    System.out.println("33333333333333333333");
                    // Deleting publication
                    String publicationImageStorageName = saved.getCoverImage().getFileStorageName();
                    publicationRepo.delete(saved);

                    System.out.println("44444444444444444444");
                    // Deleting cover image
//                    fileStorageService.deleteFile(publicationImageStorageName);

                    return 200;
                }

                // Unauthorized
                log.error("Unauthorized request (PublicationServiceImpl.deletePublication)");
                throw new UserUnauthorizedRequestException("Unauthorized request");
            }

            // User not found
            log.error("User not found (PublicationServiceImpl.deletePublication)");
            throw new UserNotFoundException("User not found");
        }

        // Publication not found
        log.error("Publication not found (PublicationServiceImpl.deletePublication)");
        throw new PublicationNotFoundException("Publication not found");
    }

    @Override
    public int changeUpVoteStatus(Long publicationId, String username) {

        Optional<Publication> publicationOptional = publicationRepo.findById(publicationId);
        Optional<User> userOptional = userRepo.findByUsername(username);

        if (publicationOptional.isPresent()) {
            // Publication exist
            if (userOptional.isPresent()) {
                // User exist
                Publication publication = publicationOptional.get();

                // Searching user in upVotes && DownVotes data
                boolean alreadyUpVoted = publication.getUpVotes().contains(userOptional.get());

                // Getting the upVotes list
                List<User> upvotesList = publication.getUpVotes();

                if (alreadyUpVoted) {
                    // User has upVoted this publication

                    // Removing user from the list
                    upvotesList.remove(userOptional.get());
                    // Setting the new list to the publication
                    publication.setUpVotes(upvotesList);

                    // Subtract total upVotes
                    publication.setTotalUpVotes(publication.getTotalUpVotes() - 1);

                    // Updating record
                    Publication savedPublication = publicationRepo.save(publication);

                    return savedPublication.getUpVotes().size();
                } else {
                    // User has not upVoted this publication

                    // Check the user in the downVotes list and removing it
                    List<User> downVotesList = publication.getDownVotes();
                    if (downVotesList.contains(userOptional.get())) {
                        // User has down-voted this publication. removing and updating the downVotes list
                        downVotesList.remove(userOptional.get());
                        publication.setDownVotes(downVotesList);

                        // Subtract total downVotes
                        publication.setTotalDownVotes(publication.getTotalDownVotes() - 1);
                    }

                    // Updating the upVotes list
                    upvotesList.add(userOptional.get());
                    publication.setUpVotes(upvotesList);

                    // Subtract total upVotes
                    publication.setTotalUpVotes(publication.getTotalUpVotes() + 1);

                    Publication savedPublication = publicationRepo.save(publication);

                    return savedPublication.getUpVotes().size();
                }
            }

            // User not found
            log.error("User not found (PublicationServiceImpl.changeUpVoteStatus)");
            throw new UserNotFoundException("User not found");
        }

        // Publication not found
        log.error("Publication not found (PublicationServiceImpl.changeUpVoteStatus)");
        throw new PublicationNotFoundException("Publication not found");
    }

    @Override
    public int changeDownVoteStatus(Long publicationId, String username) {
        Optional<Publication> publicationOptional = publicationRepo.findById(publicationId);
        Optional<User> userOptional = userRepo.findByUsername(username);

        if (publicationOptional.isPresent()) {
            // Publication exist
            if (userOptional.isPresent()) {
                // User exist
                Publication publication = publicationOptional.get();

                List<User> downvotesList = publication.getDownVotes();

                // Searching user in upVotes && DownVotes data
                boolean alreadyDownVoted = publication.getDownVotes().contains(userOptional.get());

                if (alreadyDownVoted) {
                    // User has downVoted this publication

                    // Removing user from the list
                    downvotesList.remove(userOptional.get());
                    // Setting the new list to the publication
                    publication.setDownVotes(downvotesList);

                    // Subtract total downVotes
                    publication.setTotalDownVotes(publication.getTotalDownVotes() - 1);

                    // Updating record
                    Publication savedPublication = publicationRepo.save(publication);

                    return savedPublication.getDownVotes().size();
                } else {
                    // User ain't downVote this publication

                    // Checking user in upvotes list
                    List<User> upvotesList = publication.getUpVotes();
                    if (upvotesList.contains(userOptional.get())) {
                        upvotesList.remove(userOptional.get());
                        publication.setUpVotes(upvotesList);

                        // Subtract total upVotes
                        publication.setTotalUpVotes(publication.getTotalUpVotes() - 1);
                    }

                    downvotesList.add(userOptional.get());
                    publication.setDownVotes(downvotesList);

                    // Subtract total downVotes
                    publication.setTotalDownVotes(publication.getTotalDownVotes() + 1);

                    Publication savedPublication = publicationRepo.save(publication);

                    return  savedPublication.getDownVotes().size();
                }

            }

            // User not found
            log.error("User not found (PublicationServiceImpl.changeDownVoteStatus)");
            throw new UserNotFoundException("User not found");
        }

        // Publication not found
        log.error("Publication not found (PublicationServiceImpl.changeDownVoteStatus)");
        throw new PublicationNotFoundException("Publication not found");

    }

    @Override
    public List<PublicationDto> search(Integer size, Integer page, String query, String sortParameter, String directionParameter) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "totalUpVotes");

        if (directionParameter != null) {
            direction = Sort.Direction.fromString(directionParameter);
        }

        if (sortParameter != null) {
            sort = Sort.by(direction, sortParameter);
        }

        if (query == null) {
            // Setting the sql query part to be empty.
            query = "empty";
        }

        if (size == null) {
            size = 5;
        }

        if (page == null) {
            page = 0;
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        // The page itself that we are looking for that holds the data we wanted to search it
        Page<Publication> papersPage = publicationRepo.search(query, pageRequest);

        List<PublicationDto> publicationDtos = new ArrayList<>();
        for (Publication publication : papersPage.getContent()) {
            publicationDtos.add(publicationToPublicationDtoConverter.convert(publication));
        }

        if (publicationDtos.size() == 0) {
            // Error, no publications found 404
            throw new PublicationNotFoundException("No publications been not found");
        }

        // Success
        return publicationDtos;
    }

    @Override
    public List<PublicationDto> searchByCategory(String category, Integer page) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "totalUpVotes");

        if (page == null) {
            page = 0;
        }

        PageRequest pageRequest = PageRequest.of(page, 4, sort);

        // The page itself that we are looking for that holds the data we wanted to search it
        Page<Publication> papersPage = publicationRepo.searchByCategory(category, pageRequest);

        List<PublicationDto> publicationDtos = new ArrayList<>();
        for (Publication publication : papersPage.getContent()) {
            publicationDtos.add(publicationToPublicationDtoConverter.convert(publication));
        }

        if (publicationDtos.size() == 0) {
            // Catching error
            String errorMsg = "No publications been found for " + category;
            log.error("No publications found by this category (PublicationServiceImpl.searchByCategory)");
            throw new PublicationNotFoundException(errorMsg);
        }

        return publicationDtos;
    }

    private boolean isAcceptableImageFormat(MultipartFile image) {
        return image.getContentType().equals("image/jpeg") ||
                image.getContentType().equals("image/png") ||
                image.getContentType().equals("image/webp");
    }
}
