package io.github.guardjo.pharmacyexplorer.config;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvConfig {
    @Bean
    public CsvSchema csvSchema() {
        return CsvSchema.builder()
                .addColumn("도로명전체주소", CsvSchema.ColumnType.STRING)
                .addColumn("사업장명", CsvSchema.ColumnType.STRING)
                .addColumn("위도", CsvSchema.ColumnType.STRING)
                .addColumn("경도", CsvSchema.ColumnType.STRING)
                .setSkipFirstDataRow(true)
                .build();
    }

    @Bean
    public CsvMapper csvMapper() {
        return new CsvMapper();
    }
}
