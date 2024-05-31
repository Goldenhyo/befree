package com.project.befree.controller;

import com.project.befree.domain.Place;
import com.project.befree.domain.Trip;
import com.project.befree.dto.PlanRequestDTO;
import com.project.befree.dto.TripListResponseDTO;
import com.project.befree.dto.TripRequestDTO;
import com.project.befree.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/trip")
public class TripController {

    private final TripService tripService;

    // 여행 생성
    @PostMapping("/{email}")
    public Map<String, Long> createTrip(@PathVariable String email, @RequestBody TripRequestDTO tripRequestDTO) {
        log.info("************* TripController.java / method name : createTrip / tripRequestDTO : {}", tripRequestDTO);
        Long tid = tripService.add(email, tripRequestDTO);
        log.info("************* TripController.java / method name : createTrip / tid : {}", tid);
        return Map.of("RESULT", tid);
    }

    // 여행 목록 조회
    @GetMapping("/{email}/{page}")
    public Map<String, TripListResponseDTO> readTrip(@PathVariable String email, @PathVariable int page) {
        log.info("************* TripController.java / method name : readTrip / email : {}", email);
        log.info("************* TripController.java / method name : readTrip / page : {}", page);
        TripListResponseDTO tripListResponseDTO = tripService.list(email, page);
        log.info("************* TripController.java / method name : readTrip / list : {}", tripListResponseDTO);
        return Map.of("RESULT", tripListResponseDTO);
    }

    // 여행 삭제
    @DeleteMapping("/{tid}")
    public Map<String, Boolean> deleteTrip(@PathVariable Long tid) {
        boolean deleteResult = tripService.delete(tid);
        log.info("************* TripController.java / method name : deleteTrip / deleteResult : {}", deleteResult);
        return Map.of("RESULT", deleteResult);
    }

    // 여행 상세 조회 (여행지 목록)
    @GetMapping("/detail/{tid}")
    public Map<String, List<Place>> readTripDetail(@PathVariable Long tid) {
        List<Place> plan = tripService.getPlan(tid);
        log.info("************* TripController.java / method name : readTripDetail / plan : {}", plan);
        return Map.of("RESULT", plan);
    }

    // 여행 상세 수정 (순서 이동, 날짜 이동, 여행지 삭제)
    @PutMapping("/detail/{tid}")
    public Map<String, Boolean> updateTripDetail(@PathVariable Long tid, @RequestBody PlanRequestDTO planRequestDTO) {
        boolean updateResult = tripService.putPlan(tid, planRequestDTO);
        log.info("************* TripController.java / method name : updateTripDetail / updateResult : {}", updateResult);
        return Map.of("RESULT", updateResult);
    }

    // 여행지 목록에 추가
    @PutMapping("/place/{tid}")
    public void addPlace(@PathVariable Long tid, @RequestBody PlanRequestDTO planRequestDTO) {
        boolean addPlace = tripService.addPlace(tid, planRequestDTO);
        log.info("************* TripController.java / method name : addPlace / addPlace : {}", addPlace);
    }


}
