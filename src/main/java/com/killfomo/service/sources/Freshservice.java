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
public class Freshservice extends AbstractFreshworksPuller{

    private static final String BASE_URL = "https://{domain}.freshservice.com";

    @Override
    String getUrl(String domain) {
        return BASE_URL.replace("{domain}", domain)  + "/itil/it_tasks.json";
    }

    @Override
    TaskType getType() {
        return TaskType.FRESHSERVICE;
    }

    @Override
    boolean checkFilter(Map mytask) {
        return mytask.get("closed_at") == null;
    }

    @Override
    void map(String domain, DateTimeFormatter formatter, Map<String, Object> myTaskMap, Task task) {
        task.setExternalCreatedAt(Instant.from(formatter.parse(myTaskMap.get("created_at").toString())));
        if(myTaskMap.get("due_by") != null) {
            task.setDueBy(Instant.from(formatter.parse(myTaskMap.get("due_by").toString())));
        }
        task.setExternalLink(BASE_URL.replace("{domain}", domain) + "/helpdesk/tickets/" + myTaskMap.get("ticket_id") + "#tasks");
        task.setType(TaskType.FRESHSERVICE);
        task.setSubject((String) myTaskMap.get("title"));
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
