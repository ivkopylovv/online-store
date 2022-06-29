package com.onlinestore.onlinestore.utility;

import com.onlinestore.onlinestore.constants.ProductOption;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageHelper {

    public static Pageable getPageableScript(int page) {
        return PageRequest.of(
                page,
                ProductOption.PAGE_COUNT);
    }

    public static Pageable getPageableScriptWithSort(int page, boolean asc, String attribute) {
        return PageRequest.of(
                page,
                ProductOption.PAGE_COUNT,
                SortHelper.getSortScript(asc, attribute));
    }
}
