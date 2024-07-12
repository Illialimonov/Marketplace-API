package com.ilusha.marketplaceAPI.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPassDTO {
    private String email;
    private String code;
    private String password;
    private String repeatedPassword;
}
