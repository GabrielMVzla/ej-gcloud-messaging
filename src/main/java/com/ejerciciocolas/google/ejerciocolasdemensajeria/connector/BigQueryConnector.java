package com.ejerciciocolas.google.ejerciocolasdemensajeria.connector;

import com.google.cloud.bigquery.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BigQueryConnector {

    private static final String DATASET_LOCATION = "US"; //(varias regiones)

    private final BigQuery bigQuery;
    private final String dataSetName;

    public BigQueryConnector(BigQuery bigQuery,
                             @Value("${spring.cloud.gcp.bigquery.dataset-name}") String dataSetName) {
        this.bigQuery = bigQuery;
        this.dataSetName = dataSetName;
    }

    public void uploadToBigQuery(File csvFile, String table, boolean overwrite, List<Pair<String, String>> columns) {
        log.info("Upload CSV file [{}] to BigQuery table [{}/{}]", csvFile.getPath(), dataSetName, table);

        try {
            // Fields
            List<Field> fields = columns.stream()
                    .map(c -> Field.of(c.getFirst(), StandardSQLTypeName.valueOf(c.getSecond())))
                    .collect(Collectors.toList());

            TableId tableId = TableId.of(dataSetName, table);
            WriteChannelConfiguration writeChannelConfiguration =
                    WriteChannelConfiguration
                            .newBuilder(tableId)
                            .setFormatOptions(FormatOptions.csv())
                            .setCreateDisposition(JobInfo.CreateDisposition.CREATE_IF_NEEDED)
                            .setWriteDisposition(overwrite ? JobInfo.WriteDisposition.WRITE_TRUNCATE : JobInfo.WriteDisposition.WRITE_APPEND)
                            .setSchema(Schema.of(fields))
                            .build();

            // The location must be specified; other fields can be auto-detected.
            JobId jobId = JobId.newBuilder()
                    .setLocation(DATASET_LOCATION)
                    .build();
            TableDataWriteChannel writer =
                    bigQuery
                    .writer(jobId, writeChannelConfiguration);

            // Write data to writer
            try (OutputStream stream = Channels.newOutputStream(writer)) {
                Files.copy(csvFile.toPath(), stream);
            }

            // Get load job
            Job job = writer.getJob();
            job = job.waitFor();
            JobStatistics.LoadStatistics stats = job.getStatistics();
            log.info("Wrote {} records to BigQuery table [{}/{}]", stats.getOutputRows(), dataSetName, table);
        } catch (IOException | InterruptedException | BigQueryException ex) {
            log.error("Error occured during writing of file to BigQuery {} ", ex.getMessage());
        }
    }
}
