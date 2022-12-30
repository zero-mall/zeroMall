package com.teamzero.member.controller;

import com.teamzero.member.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscribe")
public class SubscribeController {

    private final SubscribeService subscribeService;


    /**
     * 구독
     */
    @GetMapping("/add")
    public ResponseEntity<Boolean> add(@RequestParam String email,
        @RequestParam String grade) {

        return ResponseEntity.ok(subscribeService.add(email, grade));
    }

    /**
     * 구독 변경
     */
    @GetMapping("/modify")
    public ResponseEntity<Boolean> modify(@RequestParam String email,
        @RequestParam Long subscribeId, @RequestParam String grade) {
        return ResponseEntity.ok(
            subscribeService.modify(email, subscribeId, grade));
    }

    /**
     * 구독 취소
     */
    @PutMapping("/cancel")
    public ResponseEntity<Boolean> cancel(@RequestParam String email,
        @RequestParam Long subscribeId) {
        return ResponseEntity.ok(
            subscribeService.cancel(email, subscribeId));
    }


}
