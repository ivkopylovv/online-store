package com.onlinestore.onlinestore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserIdPageNumberDto {
    private Long userId;
    private int pageNumber;

}
