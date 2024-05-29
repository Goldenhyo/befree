package com.project.befree.domain;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Place {

    private Long pid;
    private String contentId;
    private int days;
}
