package com.project.befree.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Place {

    private Long pid; // 같은 날 place의 순서
    private String title;
    private String contentId;
    private String contentTypeId;
    @Builder.Default
    private int days = 1;
    private List<String> facilities;
    private String mapx;
    private String mapy;

    public void changePid(Long pid) {
        this.pid = pid;
    }

}
