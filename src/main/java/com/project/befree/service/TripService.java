package com.project.befree.service;

import com.project.befree.domain.Place;
import com.project.befree.dto.TripListResponseDTO;
import com.project.befree.dto.TripRequestDTO;

import java.util.List;

public interface TripService {
    Long add(String email, TripRequestDTO tripRequestDTO);

    Boolean put(String email, TripRequestDTO tripRequestDTO, Long tid);

    Boolean share(String email, Long tid);

    TripListResponseDTO list(String email, int page);

    TripListResponseDTO sharedList(int page);

    boolean delete(Long tid);

    List<Place> getPlan(Long tid , Long page);

    boolean putPlan(Long tid, List<List<Place>> placeList);

    boolean addPlace(Long tid, List<Place> placeList);
}
