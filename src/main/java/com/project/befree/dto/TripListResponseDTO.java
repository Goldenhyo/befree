package com.project.befree.dto;

import com.project.befree.domain.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripListResponseDTO {
    private List<Trip> paginatedTrips;
    private int totalPage;

}
