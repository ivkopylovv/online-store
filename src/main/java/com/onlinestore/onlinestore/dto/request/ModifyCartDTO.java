package com.onlinestore.onlinestore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyCartDTO {
    private String username;
    private Long productId;
}
