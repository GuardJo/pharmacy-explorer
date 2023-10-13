package io.github.guardjo.pharmacyexplorer.service;

import io.github.guardjo.pharmacyexplorer.domain.Pharmacy;
import io.github.guardjo.pharmacyexplorer.repository.PharmacyRepository;
import io.github.guardjo.pharmacyexplorer.util.TestDataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PharmacyServiceTest {
    @Mock
    private PharmacyRepository pharmacyRepository;

    @InjectMocks
    private PharmacyService pharmacyService;

    @DisplayName("약국 데이터 전체 조회")
    @Test
    void test_findAllPharmacies() {
        List<Pharmacy> expected = List.of(TestDataGenerator.pharmacy());
        given(pharmacyRepository.findAll()).willReturn(expected);

        List<Pharmacy> actual = pharmacyService.findAllPharmacies();

        assertThat(actual).isEqualTo(expected);

        then(pharmacyRepository).should().findAll();
    }
}