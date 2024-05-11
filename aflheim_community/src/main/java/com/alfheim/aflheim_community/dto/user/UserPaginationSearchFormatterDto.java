package com.alfheim.aflheim_community.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPaginationSearchFormatterDto {

    private Integer size;
    private Integer page;
    private String query;
    private String sort;
    private String direction;

    public static List<String> prepareParams(String page, String size, String query, String sort, String direction) {
        List<String> params = new ArrayList<>();

        if (size.equals("null")) {
            size = null;
        }

        if (page.equals("null")) {
            page = "0";
        }

        if (query.equals("null")) {
            query = null;
        }

        if (sort.equals("null")) {
            sort = null;
        }

        if (direction.equals("null")) {
            direction = null;
        }

        params.add(page);
        params.add(size);
        params.add(query);
        params.add(sort);
        params.add(direction);

        return params;
    }

    public static UserPaginationSearchFormatterDto fromParams(String page, String size, String query, String sort, String direction) {

        // Preparing params
        List<String> preparedParams = prepareParams(page, size, query, sort, direction);

        Integer numericalSize = null;
        if (preparedParams.get(1) != null) {
            numericalSize = Integer.parseInt(preparedParams.get(1));
        }

        UserPaginationSearchFormatterDto userPaginationSearchFormatterDto =
                UserPaginationSearchFormatterDto.builder()
                        .page(Integer.valueOf(preparedParams.get(0)))
                        .size(numericalSize)
                        .query(preparedParams.get(2))
                        .sort(preparedParams.get(3))
                        .direction(preparedParams.get(4))
                        .build();

        return userPaginationSearchFormatterDto;
    }
}
