package com.project.befree.service;

import com.project.befree.domain.Place;
import com.project.befree.domain.Trip;
import com.project.befree.dto.PlanRequestDTO;
import com.project.befree.dto.TripListResponseDTO;
import com.project.befree.dto.TripRequestDTO;

import java.util.List;

public interface TripService {
    Long add(String email, TripRequestDTO tripRequestDTO);

    TripListResponseDTO list(String email, int page);

    boolean delete(Long tid);

    List<Place> getPlan(Long tid);

    boolean putPlan(Long tid, PlanRequestDTO planRequestDTO);

    boolean addPlace(Long tid, PlanRequestDTO planRequestDTO);
}
