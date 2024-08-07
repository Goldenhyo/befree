package com.project.befree.controller;

import com.project.befree.domain.Place;
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

    // 여행 수정 (제목, 기간, 지역)
    @PutMapping("/{email}/{tid}")
    public Map<String, Boolean> updateTrip(@PathVariable String email, @PathVariable Long tid, @RequestBody TripRequestDTO tripRequestDTO) {
        log.info("************* TripController.java / method name : updateTrip / tripRequestDTO : {}", tripRequestDTO);
        Boolean isSuccess = tripService.put(email, tripRequestDTO, tid);
        log.info("************* TripController.java / method name : updateTrip / tid : {}", tid);
        return Map.of("RESULT", isSuccess);
    }

    // 여행 공유하기
    @PutMapping("/{email}/{tid}/share")
    public Map<String, Boolean> shareTrip(@PathVariable String email, @PathVariable Long tid) {
        log.info("************* TripController.java / method name : updateTrip / email :{} tid : {}", email, tid);
        Boolean share = tripService.share(email, tid);
        return Map.of("RESULT", share);
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

    // 공유된 여행 목록 조회
    @GetMapping("/sharedList/{page}")
    public Map<String, TripListResponseDTO> getSharedTrip(@PathVariable int page) {
        log.info("************* TripController.java / method name : getSharedTrip / page : {}", page);
        TripListResponseDTO tripListResponseDTO = tripService.sharedList(page);
        log.info("************* TripController.java / method name : getSharedTrip / list : {}", tripListResponseDTO);
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
    @GetMapping("/detail/{tid}/{page}")
    public Map<String, List<Place>> readTripDetail(@PathVariable Long tid, @PathVariable Long page) {
        List<Place> plan = tripService.getPlan(tid, page);
        log.info("************* TripController.java / method name : readTripDetail / plan : {}", plan);
        return Map.of("RESULT", plan);
    }

    // 여행 상세 수정 (순서 이동, 날짜 이동, 여행지 삭제)
    @PutMapping("/{tid}")
    public Map<String, Boolean> updateTripDetail(@PathVariable Long tid, @RequestBody List<List<Place>> planList) {
        boolean updateResult = tripService.putPlan(tid, planList);
        log.info("************* TripController.java / method name : updateTripDetail / updateResult : {}", planList);
        return Map.of("RESULT", updateResult);
    }

    // 여행지 목록에 추가
    @PutMapping("/place/{tid}")
    public Map<String, Boolean> addPlace(@PathVariable Long tid, @RequestBody List<Place> placeList) {
        boolean addPlace = tripService.addPlace(tid, placeList);
        log.info("************* TripController.java / method name : addPlace / addPlace : {}", addPlace);

        return Map.of("RESULT", addPlace);
    }


}
