package com.alfheim.aflheim_community.controller.publication;

import com.alfheim.aflheim_community.converter.publication.StringToPublicationFormConverter;
import com.alfheim.aflheim_community.dto.publication.PublicationDto;
import com.alfheim.aflheim_community.dto.publication.PublicationForm;
import com.alfheim.aflheim_community.model.CustomError;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
    public String getPublicationsFeedPage() {

        return "publications/publications_feed_page";
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


        System.out.println("\n\nImage : "+publicationImage.getOriginalFilename() + ", " + publicationImage.getContentType());
        System.out.println("\nDATA : "+data);

        PublicationForm publicationForm = stringToPublicationFormConverter.convert(data);
        publicationForm.setCoverImage(publicationImage);

        Long result = publicationService.addPublication(publicationForm, username);

        if (result == 500) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomError(500,
                            "Publication post request has been refused.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 1404) {
            // No report found
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new CustomError(404,
                            "User couldn't be found.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        }

        // OK
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);

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
        int result = publicationService.changeUpVoteStatus(Long.parseLong(publicationId), userDetails.getUsername());

        if (result == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomError(404,
                            "Publication not found.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 1404) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new CustomError(404,
                            "User not found.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        }

        // Success
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // Down vote publication
    @PostMapping("/publications/view/downvote")
    @ResponseBody
    public ResponseEntity<Object> changePublicationDownVoteStatus(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @RequestParam("publicationId") String publicationId) {

        int result = publicationService.changeDownVoteStatus(Long.parseLong(publicationId), userDetails.getUsername());

        if (result == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomError(404,
                            "Publication not found.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 1404) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new CustomError(404,
                            "User not found.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        }

        // Success
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    // Delete publication
    @PostMapping("/publications/delete")
    @ResponseBody
    public ResponseEntity<Object> deletePublication(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestParam("publicationId") String publicationId) {

        int result = publicationService.deletePublication(Long.parseLong(publicationId), userDetails.getUsername());

        if (result == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomError(404,
                            "Publication not found.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 1404) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new CustomError(404,
                            "User not found.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        } else if (result == 401) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CustomError(401,
                            "Unauthorized request.",
                            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                    );
        }

        // Success
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
