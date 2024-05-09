package com.alfheim.aflheim_community.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBlacklistReportForm {

    @NotBlank
    private String username;
    @NotBlank
    private String reason;
    @NotNull
    private int reputationPtsDeducted;


}
