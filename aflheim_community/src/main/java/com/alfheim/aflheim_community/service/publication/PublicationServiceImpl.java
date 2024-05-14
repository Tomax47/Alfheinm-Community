package com.alfheim.aflheim_community.service.publication;

import com.alfheim.aflheim_community.converter.publication.PublicationToPublicationDtoConverter;
import com.alfheim.aflheim_community.dto.publication.PublicationDto;
import com.alfheim.aflheim_community.dto.publication.PublicationForm;
import com.alfheim.aflheim_community.model.File.FileInfo;
import com.alfheim.aflheim_community.model.publication.Publication;
import com.alfheim.aflheim_community.model.user.User;
import com.alfheim.aflheim_community.repository.FileInfoRepo;
import com.alfheim.aflheim_community.repository.PublicationRepo;
import com.alfheim.aflheim_community.repository.UserRepo;
import com.alfheim.aflheim_community.service.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
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
        return null;
    }

    @Override
    public List<PublicationDto> searchProducts(Integer size, Integer page, String query, String sort, String direction) {
        return null;
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
                    return 500L;
                }
            }

            // Bad request. Invalid image type
            return 400L;
        }

        // User not found
        return 1404L;
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
                return 401;
            }

            // User not found
            return 1404;
        }

        // Publication not found
        return 404;
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
                    }

                    // Updating the upVotes list
                    upvotesList.add(userOptional.get());
                    publication.setUpVotes(upvotesList);

                    Publication savedPublication = publicationRepo.save(publication);

                    return savedPublication.getUpVotes().size();
                }
            }

            // User not found
            return 1404;
        }

        // Publication not found
        return 400;
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
                    }

                    downvotesList.add(userOptional.get());
                    publication.setDownVotes(downvotesList);

                    Publication savedPublication = publicationRepo.save(publication);

                    return  savedPublication.getDownVotes().size();
                }

            }

            // User not found
            return 1404;
        }

        // Publication not found
        return 404;

    }

    private boolean isAcceptableImageFormat(MultipartFile image) {
        return image.getContentType().equals("image/jpeg") ||
                image.getContentType().equals("image/png") ||
                image.getContentType().equals("image/webp");
    }
}
