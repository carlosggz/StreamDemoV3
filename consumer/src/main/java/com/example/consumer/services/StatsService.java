package com.example.consumer.services;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class StatsService {
    private int first = 0;
    private int second = 0;
    private int third = 0;

    public void incFirst() {
        first++;
    }

    public void incSecond() {
        second++;
    }

    public void incThird() {
        third++;
    }

    public void reset() {
        first = 0;
        second = 0;
        third = 0;
    }
}
