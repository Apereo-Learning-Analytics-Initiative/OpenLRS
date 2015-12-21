package org.apereo.openlrs.storage.aws.elasticsearch;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;

/**
 * 
 * @author jasonbrown
 *
 */
@Configuration
@Profile("awselasticsearch")
public class AwsElasticsearchConfig {
    private Logger log = LoggerFactory.getLogger(AwsElasticsearchConfig.class);
    @Value("${aws.es.connectionUrl}")
    private String connectionUrl;
    @Bean
    public JestClient jestClient() throws Exception {

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(connectionUrl)
                .multiThreaded(true)
                .discoveryFrequency(60, TimeUnit.SECONDS)
                .build());
        return factory.getObject();
    }
    /**
     * By elasticsearch design if a mapping already exists then it will not update with a new mapping.
     * It is also by design of the app that we do not want to delete a mapping that already exists, 
     * as multiple apps can hit the same elasticsearch endpoint. 
     */
    @PostConstruct
    public void initStatement() throws IOException, ParseException {
        try {
            jestClient().execute(new CreateIndex.Builder("openlrsstatement").build());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        URL loadedResource = this.getClass().getClassLoader().getResource("statement.mapping");
        InputStream inputStream = loadedResource.openStream();
        InputStreamReader statementFileReader = new InputStreamReader(inputStream);
        JSONParser jsonParser = new JSONParser();
        JSONObject statementJSONObject = (JSONObject) jsonParser.parse(statementFileReader);
        log.debug("statment {}",  statementJSONObject.toJSONString());
        PutMapping statmentMapping = new PutMapping.Builder(
                "openlrsstatement",
                "statement",
                statementJSONObject.toJSONString()
        ).build();

        try {
            JestResult result = jestClient().execute(statmentMapping);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void initMetadata() throws IOException, ParseException {
        try {
            jestClient().execute(new CreateIndex.Builder("openlrsstatementmetadata").build());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
        URL loadedResource = this.getClass().getClassLoader().getResource("metadata.mapping");
        InputStream inputStream = loadedResource.openStream();
        InputStreamReader metadataFileReader = new InputStreamReader(inputStream);
        JSONParser jsonParser = new JSONParser();
        JSONObject metadataJSONObject = (JSONObject) jsonParser.parse(metadataFileReader);
        PutMapping metadataMapping = new PutMapping.Builder(
                "openlrsstatementmetadata",
                "statement_metadata",
                metadataJSONObject.toJSONString()
        ).build();

        try {
            JestResult result = jestClient().execute(metadataMapping);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
    }
}
