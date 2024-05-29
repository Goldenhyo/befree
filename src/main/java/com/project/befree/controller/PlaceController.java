package com.project.befree.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/trip/place")
public class PlaceController {

    // 여행 목록
    @GetMapping("/place/{email}/{tid}")
    public void readTripDetail(@PathVariable String email, @PathVariable Long tid) {

    }
}
