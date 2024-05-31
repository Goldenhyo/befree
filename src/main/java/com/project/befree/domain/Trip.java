package com.project.befree.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Trip {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;
    private String ttitle;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tbegin;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate tend;
    private String tregion;

    @ManyToOne
    private Member member;

    @ElementCollection
    @Builder.Default
    private List<Place> placeList = new ArrayList<>();

    public Trip replace(List<Place> planList){
        this.getPlaceList().clear();
        this.getPlaceList().addAll(planList);
        return this;
    }
}
