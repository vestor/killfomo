package com.killfomo.service.sources;

import com.killfomo.domain.Task;
import com.killfomo.domain.enumeration.TaskType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by manishs on 17/05/18.
 */
@Component
public class Trello extends AbstractFreshworksPuller {
    private final String BASE_URL = "https://api.trello.com/1";

    @Value("${trello.clientkey:8917c07913f510bb463efe9536a496d7}")
    private String CLIENT_KEY;

    @Override
    String getUrl(String domain, String apiKey) {
        return BASE_URL + "/members/me/cards?fields=labels,name,dateLastActivity,shortUrl,due,desc,closed" +
            "&key=" + CLIENT_KEY + "&token=" + apiKey;
    }

    @Override
    TaskType getType() {
        return TaskType.TRELLO;
    }

    @Override
    boolean checkFilter(Map mytask) {
        return Objects.equals(mytask.get("closed").toString(), "false");
    }

    @Override
    void map(String domain, DateTimeFormatter formatter, Map<String, Object> myTaskMap, Task task) {
        task.setExternalCreatedAt(Instant.from(formatter.parse(myTaskMap.get("dateLastActivity").toString().substring(0,19) + "-00:00")));
        if(myTaskMap.get("due") != null) {
            task.setDueBy(Instant.from(formatter.parse(myTaskMap.get("due").toString().substring(0,19)+ "-00:00")));
        }
        task.setExternalLink(myTaskMap.get("shortUrl").toString());
        task.setType(TaskType.TRELLO);
        task.setSubject((String) myTaskMap.get("name"));
    }

    @Override
    protected List parse(ResponseEntity<String> rawResponse) throws IOException {
        return killfomoJsonMapper.readValue(rawResponse.getBody(), List.class);
    }

    @Override
    protected HttpHeaders getHttpHeaders(String apiKey) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    @Override
    public DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    }

}
