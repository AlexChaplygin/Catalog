package ru.reksoft.chaplygin.catalog.dto;

import lombok.*;
import ru.reksoft.chaplygin.catalog.entity.User;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserForSaveDTO {

    private User user;
    private TokenObjectDTO token;
    private String sectorIds;
}
