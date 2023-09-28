package org.example.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private int userId;
    private String username;
    private  String password;
}
