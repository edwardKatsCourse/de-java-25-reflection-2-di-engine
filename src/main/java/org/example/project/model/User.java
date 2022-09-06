package org.example.project.model;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class User {

    private String id;
    private String firstName;
    private String lastName;

    private LocalDate dateOfBirth;
}
