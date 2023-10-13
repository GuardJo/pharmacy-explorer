package io.github.guardjo.pharmacyexplorer.util;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DistanceCalculatorTest {
    private final DistanceCalculator distanceCalculator = new DistanceCalculator();

    @DisplayName("두 지점 사이의 거리 계산")
    @Test
    void test_calculateDistanceByHaversine() {
        // 서울 위/경도
        double x1 = 37.547889;
        double y1 = 126.997128;

        // 부산 위/경도
        double x2 = 35.158874;
        double y2 = 129.043846;

        double expected = 322.72; // 두 도시 사이의 거리 (GoogleMap 기준)

        double actual = distanceCalculator.calculateDistanceByHaversine(x1, y1, x2, y2);

        assertThat(actual).isCloseTo(expected, Offset.offset(0.1));
    }
}