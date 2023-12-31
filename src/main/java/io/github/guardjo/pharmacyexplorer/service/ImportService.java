package io.github.guardjo.pharmacyexplorer.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.github.guardjo.pharmacyexplorer.dto.PharmacyVo;
import io.github.guardjo.pharmacyexplorer.repository.PharmacyRepository;
import io.github.guardjo.pharmacyexplorer.repository.SearchInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImportService {
    private final CsvSchema csvSchema;
    private final CsvMapper csvMapper;
    private final PharmacyRepository pharmacyRepository;
    private final SearchInfoRepository searchInfoRepository;

    private final static String PHARMACY_DATA_FILE_NAME = "pharmacy_data.csv";

    /**
     * 약국 데이터(csv) 적재
     */
    @EventListener(classes = ApplicationReadyEvent.class)
    @Transactional
    public void initPharmacyData() {
        log.info("Initializing Pharmacy Data...");
        int initializedCount = 0;

        searchInfoRepository.deleteAll();
        pharmacyRepository.deleteAll();

        try {
            File csvData = readFile();
            List<PharmacyVo> pharmacyVoList = parseFrom(csvData);
            initializedCount = savePharmacies(pharmacyVoList);

        } catch (IOException e) {
            log.error("Failed Initialize Pharmacy Data, ", e);
        }

        log.info("Initialized Pharmacy Data, (size : {})", initializedCount);
    }

    private File readFile() throws IOException {
        Resource resource = new ClassPathResource(PHARMACY_DATA_FILE_NAME);

        try (InputStream inputStream = resource.getInputStream()) {
            File copyDataFile = File.createTempFile("pharmacy_data_" + System.currentTimeMillis(), ".csv");
            FileCopyUtils.copy(inputStream.readAllBytes(), copyDataFile);

            return copyDataFile;
        } catch (Exception e) {
            log.error("Exception : ", e);
            return null;
        }
    }

    private List<PharmacyVo> parseFrom(File csvFile) {
        List<PharmacyVo> pharmacyVoList = new ArrayList<>();

        if (Objects.isNull(csvFile)) {
            log.error("Not Found CSV data");
            return List.of();
        }

        try (MappingIterator<PharmacyVo> mappingIterator = csvMapper.readerFor(PharmacyVo.class).with(csvSchema)
                .readValues(csvFile)) {
            pharmacyVoList = mappingIterator.readAll();

        } catch (IOException e) {
            log.error("CSV Parsing Error, ", e);
        } finally {
            csvFile.deleteOnExit();
        }

        return pharmacyVoList;
    }

    private int savePharmacies(List<PharmacyVo> pharmacyVoList) {
        return pharmacyRepository.saveAll(pharmacyVoList.stream()
                .map(PharmacyVo::toEntity)
                .collect(Collectors.toList())
        ).size();
    }
}
