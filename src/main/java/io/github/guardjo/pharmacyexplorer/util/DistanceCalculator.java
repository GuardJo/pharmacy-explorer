package io.github.guardjo.pharmacyexplorer.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DistanceCalculator {
    /**
     * <h3>Haversine 공식</h3>
     * <hr>
     * 구체 위 두 지점 사이의 거리를 구하는 공식
     * <a href="https://en.wikipedia.org/wiki/Haversine_formula">Haversine wiki</a>
     *
     * @param x1 base x 좌표
     * @param y1 base y 좌표
     * @param x2 target x 좌표
     * @param y2 target y 좌표
     * @return 뒤 지점 사이의 거리(km)
     */
    public double calculateDistanceByHaversine(double x1, double y1, double x2, double y2) {
        double distance;
        double earthRadius = 6371; // 지구 반지름(km)
        double toRadian = Math.PI / 180;

        double deltaLatitude = Math.abs(x1 - x2) * toRadian;
        double deltaLongitude = Math.abs(y1 - y2) * toRadian;

        double sinDeltaLatitude = Math.sin(deltaLatitude / 2);
        double sinDeltaLongitude = Math.sin(deltaLongitude / 2);
        double squareRoot = Math.sqrt(
                sinDeltaLatitude * sinDeltaLatitude + Math.cos(x1 * toRadian) * Math.cos(x2 * toRadian)
                        * sinDeltaLongitude * sinDeltaLongitude);

        distance = 2 * earthRadius * Math.asin(squareRoot);

        log.info("Calculated distance, ({}, {}) <-> ({}, {})", x1, y1, x2, y2);

        return distance;
    }
}
