package com.lap.Order.Management.System.tache;

import com.lap.Order.Management.System.auth.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    List<Task> findByAssignee(User assignee);

}