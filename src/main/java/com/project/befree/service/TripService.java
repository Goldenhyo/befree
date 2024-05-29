package com.project.befree.service;

import com.project.befree.domain.Place;
import com.project.befree.domain.Trip;
import com.project.befree.dto.PlanRequestDTO;
import com.project.befree.dto.TripRequestDTO;

import java.util.List;

public interface TripService {
    Long add(String email, TripRequestDTO tripRequestDTO);

    List<Trip> list(String email);

    boolean delete(Long tid);

    List<Place> getPlan(Long tid);

    boolean putPlan(Long tid, PlanRequestDTO planRequestDTO);

    boolean addPlace(Long tid, PlanRequestDTO planRequestDTO);
}
