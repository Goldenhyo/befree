package com.project.befree.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String name;

    public void changePassword(String password) {
        this.password = password;
    }

}
