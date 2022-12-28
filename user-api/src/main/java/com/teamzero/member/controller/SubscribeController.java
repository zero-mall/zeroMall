package com.teamzero.member.controller;

import com.teamzero.member.service.SubscribeService;
import lombok.RequiredArgsConstructor;
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
    public boolean addSubscribe(@RequestParam String email,
        @RequestParam String grade) {

        return subscribeService.addSubscribe(email, grade);
    }

    /**
     * 구독 변경
     */
    @GetMapping("/modify")
    public boolean modifySubscribe(@RequestParam String email,
        @RequestParam Long subscribeId, @RequestParam String grade) {

        return subscribeService.modifySubscribe(email, subscribeId, grade);

    }

    /**
     * 구독 취소
     */
    @PutMapping("/cancel")
    public boolean cancelSubscribe(@RequestParam String email,
        @RequestParam Long subscribeId) {

        return subscribeService.cancelSubscribe(email, subscribeId);

    }


}
