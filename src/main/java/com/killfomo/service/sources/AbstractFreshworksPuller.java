package com.killfomo.service.sources;

import com.killfomo.domain.AuthResource;
import com.killfomo.domain.Task;
import com.killfomo.domain.User;
import com.killfomo.domain.enumeration.TaskState;
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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by manishs on 17/05/18.
 */
public abstract class AbstractFreshworksPuller {

    final Logger log = LoggerFactory.getLogger(getClass());

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

    private void fetchTaskForUser(Long userId) throws IOException {

        AuthResource authResource = authResourceService.findByUserIdAndType(userId, getType());
        if(authResource != null) {
            Map rawTokenInfo = killfomoJsonMapper.readValue(authResource.getToken(), Map.class);
            fetchTasks(userId, rawTokenInfo);
        }
    }

    abstract String getUrl(String domain, String apiKey);
    abstract TaskType getType();
    abstract boolean checkFilter(Map mytask);
    abstract void map(String domain, DateTimeFormatter formatter, Map<String, Object> myTaskMap, Task task);
    public DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    }


    @Scheduled(fixedDelay = 5000)
    public void fetchTasksForEveryone() throws IOException {

        List<User> users = userRepository.findAll();
        for(User user : users) {
            fetchTaskForUser(user.getId());
        }

    }

    protected void fetchTasks(Long userId, Map rawTokenInfo) throws IOException {
        String domain = (String) rawTokenInfo.get("domain");
        String apiKey = (String) rawTokenInfo.get("key");
        HttpHeaders headers = getHttpHeaders(apiKey);


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
        List<String> idsToRetain = new ArrayList<>();
        for(Object mytask : mytasks) {

            Map<String, Object> myTaskMap = (((Map<String,Object>)mytask));
            String id = userId + "-" + getType() + "-" + myTaskMap.get("id");
            if(checkFilter(myTaskMap)) {
                Task task = new Task();
                task.setId(id);
                idsToRetain.add(id);
                Task one = taskRepository.findOne(id);
                if(one != null) {
                    task.setState(one.getState());
                } else {
                    task.setState(TaskState.TODO);
                }
                task.setUserId(userId);
                task.setCustomJson(killfomoJsonMapper.writeValueAsString(mytask));
                task.setType(getType());

                map(domain, formatter, myTaskMap, task);
                tasksToReturn.add(task);
            }
        }
        List<String> taskList = taskRepository.findIdByTypeAndState(getType(), TaskState.WIP);
        tasksToReturn.stream().filter(a -> taskList.contains(a.getId())).forEach(b -> b.setState(TaskState.WIP));
        taskRepository.markAsDone(idsToRetain, getType());
        taskRepository.save(tasksToReturn);
    }

    protected abstract List parse(ResponseEntity<String> rawResponse) throws IOException;

    protected abstract HttpHeaders getHttpHeaders(String apiKey);


}
