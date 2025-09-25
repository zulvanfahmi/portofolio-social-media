package com.portofolio.socialMedia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ListUserDTO {
    
    private String profile_image_url;
    private String name;
    private String username;
    private Long id_user_list;

}
