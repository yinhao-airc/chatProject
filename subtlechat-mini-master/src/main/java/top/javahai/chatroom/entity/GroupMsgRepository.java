package top.javahai.chatroom.entity;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *在线用户仓库，存储在线用户
 */
@Component
public class GroupMsgRepository {
    private List<GroupMsgContent> groupMsgContentList= new CopyOnWriteArrayList();

    public List<GroupMsgContent> getGroupMsgContentList() {
        return groupMsgContentList;
    }

    public void setGroupMsgContentList(List<GroupMsgContent> groupMsgContentList) {
        this.groupMsgContentList = groupMsgContentList;
    }

    public void addGroupMsgContent(GroupMsgContent groupMsgContent){
        groupMsgContentList.add(groupMsgContent);

    }
}
