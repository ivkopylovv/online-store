package com.onlinestore.onlinestore.utility;

import org.springframework.data.domain.Sort;

public class SortHelper {

    public static Sort getSortScript(boolean asc, String attribute) {
        return asc ? Sort.by(attribute).ascending() : Sort.by(attribute).descending();
    }
}
