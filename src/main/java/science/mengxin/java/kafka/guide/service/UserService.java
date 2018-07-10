package science.mengxin.java.kafka.guide.service;

import java.util.List;

public interface UserService {
    Integer findUserId(String userName);
    List<String> findAllUsers();
}
