package com.alfheim.aflheim_community.service.admin;

import com.alfheim.aflheim_community.dto.user.UserBlacklistReportDto;
import com.alfheim.aflheim_community.dto.user.UserBlacklistReportForm;
import com.alfheim.aflheim_community.dto.user.UserDto;
import com.alfheim.aflheim_community.dto.user.UserUpdateForm;
import com.alfheim.aflheim_community.model.user.BlacklistRecordState;

import java.util.List;

public interface AdminUsersCRUDService {

    List<UserDto> search(Integer size, Integer page, String query, String sort, String direction);

    int deleteUser(String username);

    int resetUserPassword(String username, String newPassword, String adminUsername);

    int confirmUserAccount(String username);

    int changeBanUserAccountState(String username, String adminUsername);

    int changeSuspensionUserAccountState(String username, String adminUsername);

    int addUserToBlacklist(UserBlacklistReportForm userBlacklistReportForm, String adminUsername);

    // Reverting error request return the deducted pts to the user
    void removeUserFromBlacklist(String username, boolean isRevertingErrorReq);

    UserBlacklistReportDto getUserBlacklistReportDetails(String username);

    UserDto updateUserProfileInfo(String username, UserUpdateForm userUpdateForm);
}
