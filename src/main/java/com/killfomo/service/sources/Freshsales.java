package com.killfomo.service.sources;

import com.killfomo.domain.Task;
import com.killfomo.domain.enumeration.TaskType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Created by manishs on 16/05/18.
 */
@Component
public class Freshsales extends AbstractFreshworksPuller{


    private static final String BASE_URL = "https://{domain}.freshsales.io";

    @Override
    String getUrl(String domain, String apiKey) {
        return BASE_URL.replace("{domain}", domain)  + "/api/tasks?filter=open";
    }

    @Override
    TaskType getType() {
        return TaskType.FRESHSALES;
    }

    @Override
    boolean checkFilter(Map mytask) {
        return true;
    }

    @Override
    void map(String domain, DateTimeFormatter formatter, Map<String, Object> myTaskMap, Task task) {
        task.setExternalCreatedAt(Instant.parse(myTaskMap.get("created_at").toString()));
        if(myTaskMap.get("due_by") != null) {
            task.setDueBy(Instant.parse(myTaskMap.get("due_by").toString()));
        }
        task.setExternalLink(BASE_URL.replace("{domain}", domain) + "/calendar/tasks");
        task.setType(TaskType.FRESHSALES);
        task.setSubject((String) myTaskMap.get("title"));
    }

    @Override
    protected List parse(ResponseEntity<String> rawResponse) throws IOException {
        return (List)killfomoJsonMapper.readValue(rawResponse.getBody(), Map.class).get("tasks");
    }


    @Override
    public HttpHeaders getHttpHeaders(String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token token=" + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


}
