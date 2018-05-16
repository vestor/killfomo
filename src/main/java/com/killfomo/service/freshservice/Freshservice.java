package com.killfomo.service.freshservice;

import com.killfomo.domain.AuthResource;
import com.killfomo.domain.Task;
import com.killfomo.domain.User;
import com.killfomo.domain.enumeration.TaskType;
import com.killfomo.repository.AuthResourceRepository;
import com.killfomo.repository.TaskRepository;
import com.killfomo.repository.UserRepository;
import com.killfomo.service.mapper.KillfomoJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.killfomo.service.util.HttpUtils.getHttpHeadersForBasic;

/**
 * Created by manishs on 16/05/18.
 */
@Component
public class Freshservice {

    private final Logger log = LoggerFactory.getLogger(Freshservice.class);

    private static final String BASE_URL = "https://{domain}.freshservice.com";

    @Autowired
    AuthResourceRepository authResourceService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    KillfomoJsonMapper killfomoJsonMapper;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;



    public void fetchTaskForUser(Long userId) throws IOException {

        AuthResource authResource = authResourceService.findByUserIdAndTaskType(userId, TaskType.FRESHSERVICE);
        if(authResource != null) {
            Map rawTokenInfo = killfomoJsonMapper.readValue(authResource.getToken(), Map.class);
            fetchTasksFromFreshservice(userId, rawTokenInfo);
        }
    }


    @Scheduled(fixedDelay = 5000)
    public void fetchTasksForEveryone() throws IOException {

        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now());
        for(User user : users) {
            fetchTaskForUser(user.getId());
        }

    }

    private void fetchTasksFromFreshservice(Long userId, Map rawTokenInfo) throws IOException {
        String domain = (String) rawTokenInfo.get("domain");
        String apiKey = (String) rawTokenInfo.get("key");


        String url = BASE_URL.replace("{domain}", domain)  + "/itil/it_tasks.json";

        HttpHeaders headers = getHttpHeadersForBasic(apiKey);
        HttpEntity httpEntity = new HttpEntity(headers);


        ResponseEntity<String> rawResponse = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        if(rawResponse.getStatusCode() != HttpStatus.OK) {
            log.error("Got this response {} {}", rawResponse.getHeaders(), rawResponse.getBody());
            throw new RuntimeException("Unable to get Data from Freshservice");
        }

        List<Task> tasksToReturn = new ArrayList<>();

        //Dirty parsing the result
        List mytasks = killfomoJsonMapper.readValue(rawResponse.getBody(), List.class);
        for(Object mytask : mytasks) {

            Map<String, Object> myTaskMap = (((Map<String,Object>)mytask));
            if(myTaskMap.get("closed_at") != null) {
                Task task = new Task();
                task.setUserId(userId);
                task.setCustomJson(killfomoJsonMapper.writeValueAsString(mytask));
                task.setExternalCreatedAt(Instant.parse(myTaskMap.get("created_at").toString()));
                task.setDueBy(Instant.parse(myTaskMap.get("due_by").toString()));
                task.setExternalLink(BASE_URL.replace("{domain}", domain) + "/helpdesk/tickets/" + myTaskMap.get("ticket_id") + "#tasks");
                task.setType(TaskType.FRESHSERVICE);
                task.setSubject((String) myTaskMap.get("title"));
            }
        }
        taskRepository.save(tasksToReturn);
    }


}
