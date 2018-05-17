package com.killfomo.service.sources;

import com.killfomo.domain.Task;
import com.killfomo.domain.enumeration.TaskType;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Created by manishs on 16/05/18.
 */
@Component
public class Jira extends AbstractFreshworksPuller{

    private static final String BASE_URL = "https://{domain}.atlassian.net";

    @Override
    String getUrl(String domain, String apiKey) {
        return BASE_URL.replace("{domain}", domain)  + "/rest/api/2/search";
    }

    @Override
    TaskType getType() {
        return TaskType.JIRA;
    }

    @Override
    boolean checkFilter(Map mytask) {
        return ((String)((Map)((Map)mytask.get("fields")).get("status")).get("name")).replace(" ","").equalsIgnoreCase("todo");
    }

    @Override
    void map(String domain, DateTimeFormatter formatter, Map<String, Object> myTaskMap, Task task) {
        task.setExternalCreatedAt(Instant.from(formatter.parse(((Map)myTaskMap.get("fields")).get("created").toString())));
        Object dueDate = ((Map) myTaskMap.get("fields")).get("duedate");
        if(dueDate!= null) {
            task.setDueBy(Instant.from(formatter.parse(dueDate.toString())));
        }
        task.setExternalLink(BASE_URL.replace("{domain}", domain) +  "/browse/"  + (String) myTaskMap.get("key"));
        task.setType(TaskType.JIRA);
        task.setSubject((String) ((Map) myTaskMap.get("fields")).get("summary"));
        if( task.getSubject() == null) {
            task.setSubject("key");
        }
    }

    @Override
    protected List parse(ResponseEntity<String> rawResponse) throws IOException {
        Map object = killfomoJsonMapper.readValue(rawResponse.getBody(), Map.class);
        return (List) object.get("issues");
    }

    @Override
    protected HttpHeaders getHttpHeaders(String apiKey) {
        return null;
    }


    protected HttpHeaders getMyHttpHeaders(String apiKey, String email) {
        String plainCreds = email + ":"  + apiKey;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + base64Creds);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    public DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }



    @Override
    protected void fetchTasks(Long userId, Map rawTokenInfo) throws IOException {
        String domain = (String) rawTokenInfo.get("domain");
        String apiKey = (String) rawTokenInfo.get("key");
        String email = (String) rawTokenInfo.get("email");
        HttpHeaders headers = getMyHttpHeaders(apiKey, email);


        String url = getUrl(domain, apiKey);
        HttpEntity httpEntity = new HttpEntity(headers);


        ResponseEntity<String> rawResponse = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        if(rawResponse.getStatusCode() != HttpStatus.OK) {
            log.error("Got this response {} {}", rawResponse.getHeaders(), rawResponse.getBody());
            throw new RuntimeException("Unable to get Data from " + getClass());
        }

        List<Task> tasksToReturn = new ArrayList<>();

        //Dirty parsing the result
        List mytasks = parse(rawResponse);

        DateTimeFormatter formatter = getDateTimeFormatter();
        for(Object mytask : mytasks) {

            Map<String, Object> myTaskMap = (((Map<String,Object>)mytask));
            if(checkFilter(myTaskMap)) {
                Task task = new Task();
                task.setId(userId + "-" + getType() + "-" + myTaskMap.get("id"));
                task.setUserId(userId);
                task.setCustomJson(killfomoJsonMapper.writeValueAsString(mytask));
                task.setType(getType());

                map(domain, formatter, myTaskMap, task);
                tasksToReturn.add(task);
            }
        }
        taskRepository.save(tasksToReturn);
    }


}
