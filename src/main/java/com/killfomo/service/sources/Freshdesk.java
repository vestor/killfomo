package com.killfomo.service.sources;

import com.killfomo.domain.Task;
import com.killfomo.domain.enumeration.TaskType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.killfomo.service.util.HttpUtils.getHttpHeadersForBasic;

/**
 * Created by manishs on 16/05/18.
 */
@Component
public class Freshdesk extends AbstractFreshworksPuller{

    private static final String BASE_URL = "https://{domain}.freshdesk.com";

    @Override
    String getUrl(String domain) {
        return BASE_URL.replace("{domain}", domain)  + "/api/v2/tickets.json?filter=new_and_my_open";
    }

    @Override
    TaskType getType() {
        return TaskType.FRESHDESK;
    }

    @Override
    boolean checkFilter(Map mytask) {
        return Integer.parseInt((String)mytask.get("status")) != 5;
    }

    @Override
    void map(String domain, DateTimeFormatter formatter, Map<String, Object> myTaskMap, Task task) {
        task.setExternalCreatedAt(Instant.parse(myTaskMap.get("created_at").toString()));
        task.setDueBy(Instant.parse(myTaskMap.get("due_by").toString()));
        task.setExternalLink(BASE_URL.replace("{domain}", domain) + "/a/tickets/" + myTaskMap.get("ticket_id"));
    }

    @Override
    protected List parse(ResponseEntity<String> rawResponse) throws IOException {
        return killfomoJsonMapper.readValue(rawResponse.getBody(), List.class);
    }

    @Override
    protected HttpHeaders getHttpHeaders(String apiKey) {
        return getHttpHeadersForBasic(apiKey);
    }


}