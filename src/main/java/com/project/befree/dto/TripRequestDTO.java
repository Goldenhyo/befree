package com.project.befree.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripRequestDTO {
    private String ttitle;
    private LocalDateTime tbegin;
    private LocalDateTime tend;
    private String tregion;

}
