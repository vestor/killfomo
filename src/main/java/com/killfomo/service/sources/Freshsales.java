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
        return BASE_URL.replace("{domain}", domain)  + "/api/tasks?include=targetable";
    }

    @Override
    TaskType getType() {
        return TaskType.FRESHSALES;
    }

    @Override
    boolean checkFilter(Map mytask) {
        return mytask.get("status").toString().equalsIgnoreCase("0");
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
        Map rawMap = killfomoJsonMapper.readValue(rawResponse.getBody(), Map.class);
        List origData = (List)rawMap.get("tasks");
        List contactData = (List)rawMap.get("contacts");
        List dealsData = (List)rawMap.get("deals");
        List leadsData = (List)rawMap.get("leads");

        for(Map task : (List<Map>)origData) {
            Map targetable = (Map) task.get("targetable");
            if(targetable !=null) {
                String type = (String) targetable.get("type");
                if(type.equalsIgnoreCase("deal")) {
                    task.put("target", dealsData.stream().filter(a -> getEquality(targetable, (Map) a)).findFirst().get());
                } else if ( type.equalsIgnoreCase("contact")) {
                    task.put("target", contactData.stream().filter(a -> getEquality(targetable, (Map) a)).findFirst().get());
                } else if (type.equalsIgnoreCase("lead") ) {
                    task.put("target", leadsData.stream().filter(a ->getEquality(targetable, (Map) a)).findFirst().get());
                }
            }
        }

        return origData;

    }

    private boolean getEquality(Map targetable, Map a) {
        return a.get("id").toString().equalsIgnoreCase(targetable.get("id").toString());
    }


    @Override
    public HttpHeaders getHttpHeaders(String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token token=" + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


}
