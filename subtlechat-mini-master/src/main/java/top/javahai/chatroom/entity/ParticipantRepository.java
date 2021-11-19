package top.javahai.chatroom.entity;

import org.springframework.stereotype.Component;
import top.javahai.chatroom.utils.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *在线用户仓库，存储在线用户
 */
@Component
public class ParticipantRepository {

    private String currUserName;
    private Map<String, User> activeSessions = new ConcurrentHashMap<String, User>(); //在线用户map，键：用户名称，值：用户对象

    public Map<String, User> getActiveSessions() {
        return activeSessions;
    }

    public void setActiveSessions(Map<String, User> activeSessions) {
        this.activeSessions = activeSessions;
    }

    public void add(String name, User user){
        activeSessions.put(name, user);

    }

    public void delete(String name){
        activeSessions.remove(name);

    }

    public User remove(String name){
        return activeSessions.remove(name);
    }

    public boolean containsUserName(String name){
        return activeSessions.containsKey(name);
    }


    private List<String>  copyList = new CopyOnWriteArrayList();

    public List<String> getCopyList() {
        return copyList;
    }

    public String getCurrUserName() {
        return currUserName;
    }

    public void setCurrUserName(String currUserName) {
        this.currUserName = currUserName;
    }

    public void setCopyList(List<String> copyList) {
        this.copyList = copyList;
    }

    public List<User>  getUsersWithoutCurrentUser(){
        List<User> userList=new ArrayList<>();
        for(Map.Entry<String, User> entry : activeSessions.entrySet()){
            if(UserUtil.getCurrentUser().getUsername()!=entry.getKey()) {
                userList.add(entry.getValue());
            }
        }
        return userList;
    }
}
