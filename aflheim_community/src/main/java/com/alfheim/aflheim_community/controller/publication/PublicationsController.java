package com.alfheim.aflheim_community.controller.publication;

import com.alfheim.aflheim_community.converter.publication.StringToPublicationFormConverter;
import com.alfheim.aflheim_community.dto.publication.PublicationDto;
import com.alfheim.aflheim_community.dto.publication.PublicationForm;
import com.alfheim.aflheim_community.security.details.UserDetailsImpl;
import com.alfheim.aflheim_community.service.publication.PublicationService;
import com.alfheim.aflheim_community.service.user.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
public class PublicationsController {

    @Autowired
    private PublicationService publicationService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private StringToPublicationFormConverter stringToPublicationFormConverter;

    // Get feed page
    @GetMapping("/publications/feed")
    public String getPublicationsFeedPage(Model model) {

        List<PublicationDto> publicationDto = publicationService.search(null, 0, null, null, null);
        model.addAttribute("featuredPublication", publicationDto.get(0));
        publicationDto.remove(0);
        model.addAttribute("publications", publicationDto);

        return "publications/publications_feed_page";
    }

    @GetMapping("/publications/feed/pag")
    @ResponseBody
    public ResponseEntity<Object> pagSearchPublications(@RequestParam(value = "page") Integer page,
                                                        @RequestParam(value = "query", required = false) String query) {

        // Setting up the query
        if (query.equals("null")) {
            query = null;
        }

        List<PublicationDto> publicationDtos = publicationService.search(null, page, query, null, null);

        if (page == 0) {
            // Removing the featured publication
            publicationDtos.remove(0);
        }

        return ResponseEntity.ok(publicationDtos);
    }

    // Get new publication page
    @GetMapping("/profile/publications/new")
    public String getCreatePublicationPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        model.addAttribute("userDto", profileService.getProfileDetailsByUsername(userDetails.getUsername()));
        return "publications/publication_new_page";
    }


    // Add new publication
    @PostMapping("/profile/publication/add")
    @ResponseBody
    public ResponseEntity<Object> addPublication(@RequestParam("username") String username,
                                                 @RequestParam(value = "publicationImage", required = false) MultipartFile publicationImage,
                                                 @RequestParam(value = "data", required = false) String data) {

        PublicationForm publicationForm = stringToPublicationFormConverter.convert(data);
        publicationForm.setCoverImage(publicationImage);

        Long publicationId = publicationService.addPublication(publicationForm, username);

        // OK
        return ResponseEntity.status(HttpStatus.OK)
                .body(publicationId);

    }

    // View publication
    @GetMapping("/publications/view")
    public String getPublicationPage(@RequestParam("publicationId") Long publicationId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails,
                                     Model model) {

        PublicationDto publicationDto = publicationService.getPublicationById(publicationId);

        model.addAttribute("user", profileService.getProfileDetailsByUsername(userDetails.getUsername()));
        model.addAttribute("publication", publicationDto);

        return "publications/publication_page";
    }

    // Upvote publication
    @PostMapping("/publications/view/upvote")
    @ResponseBody
    public ResponseEntity<Object> changePublicationUpVoteStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @RequestParam("publicationId") String publicationId) {

        System.out.println("\n\nCONTROLLER UPVOTE : ID -> " + publicationId+"\n\n");
        int totalUpVotes = publicationService.changeUpVoteStatus(Long.parseLong(publicationId), userDetails.getUsername());

        // Success
        return ResponseEntity.status(HttpStatus.OK).body(totalUpVotes);
    }

    // Down vote publication
    @PostMapping("/publications/view/downvote")
    @ResponseBody
    public ResponseEntity<Object> changePublicationDownVoteStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @RequestParam("publicationId") String publicationId) {

        int totalDownVotes = publicationService.changeDownVoteStatus(Long.parseLong(publicationId), userDetails.getUsername());

        // Success
        return ResponseEntity.status(HttpStatus.OK).body(totalDownVotes);

    }

    // Delete publication
    @PostMapping("/publications/delete")
    @ResponseBody
    public ResponseEntity<Object> deletePublication(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestParam("publicationId") String publicationId) {

        int successStatus = publicationService.deletePublication(Long.parseLong(publicationId), userDetails.getUsername());

        // Success
        return ResponseEntity.status(HttpStatus.OK).body(successStatus);
    }

    @GetMapping("/publications/categories")
    @ResponseBody
    public ResponseEntity<Object> searchByCategory(@RequestParam(value = "page") Integer page,
                                                        @RequestParam(value = "category") String category) {


        List<PublicationDto> publicationDtos = publicationService.searchByCategory(category, page);

        return ResponseEntity.ok(publicationDtos);
    }
}
