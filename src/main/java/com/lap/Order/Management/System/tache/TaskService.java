package com.lap.Order.Management.System.tache;

import com.lap.Order.Management.System.auth.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired private TaskRepo taskRepo;

    public List<Task> getTasksByUser(User user) {
        return taskRepo.findTasksByUser(user);
    }

    public Task createTask(Task task) {
        return taskRepo.save(task);
    }

}
