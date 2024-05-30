package com.project.befree.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String name;
    private boolean status; // 0 = 탈퇴, 1 = 가입
    private boolean social;

    @OneToMany
    private List<Trip> trips;

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeStatus(boolean status) {
        this.status = status;
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }
}
