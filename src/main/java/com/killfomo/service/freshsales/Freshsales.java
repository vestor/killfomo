package com.killfomo.service.freshsales;

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
public class Freshsales {

    private final Logger log = LoggerFactory.getLogger(Freshsales.class);

    private static final String BASE_URL = "https://{domain}.freshsales.io";

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

        AuthResource authResource = authResourceService.findByUserIdAndType(userId, TaskType.FRESHSALES);
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


        String url = BASE_URL.replace("{domain}", domain)  + "/api/tasks?filter=open";

        HttpHeaders headers = getHttpHeadersForBasic(apiKey);
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<String> rawResponse = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);



        if(rawResponse.getStatusCode() != HttpStatus.OK) {
            log.error("Got this response {} {}", rawResponse.getHeaders(), rawResponse.getBody());
            throw new RuntimeException("Unable to get Data from Freshsales");
        }

        List<Task> tasksToReturn = new ArrayList<>();

        //Dirty parsing the result
        Map mytasks = killfomoJsonMapper.readValue(rawResponse.getBody(), Map.class);
        for(Object mytask : (List)mytasks.get("tasks")) {

            Map<String, Object> myTaskMap = (((Map<String,Object>)mytask));
                Task task = new Task();
                task.setUserId(userId);
                task.setCustomJson(killfomoJsonMapper.writeValueAsString(mytask));
                task.setExternalCreatedAt(Instant.parse(myTaskMap.get("created_at").toString()));
                task.setDueBy(Instant.parse(myTaskMap.get("due_by").toString()));
                task.setExternalLink(BASE_URL.replace("{domain}", domain) + "/calendar/tasks");
                task.setType(TaskType.FRESHSERVICE);
                task.setSubject((String) myTaskMap.get("title"));
        }
        taskRepository.save(tasksToReturn);
    }


}
