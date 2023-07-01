package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashMap<String,User> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashMap<String,User>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception {
        if(userMobile.containsKey(mobile)){
            throw new Exception("User already exists");
        }
        else{
            User user = new User(name, mobile);
            userMobile.put(name,user);
            return "SUCCESS";
        }
    }

    public Group createGroup(List<User> users) {
        Group group = new Group();
        if(users.size() == 2){
            User us1 = users.get(1);
            group.setNumberOfParticipants(2);
            group.setName(us1.getName());
            adminMap.put(group,users.get(0));
            groupUserMap.put(group,users);

        }
        else{
            customGroupCount++;
            group.setName("Group "+ customGroupCount);
            group.setNumberOfParticipants(users.size());
            adminMap.put(group,users.get(0));
            groupUserMap.put(group,users);

        }
        return group;
    }


    public int createMessage(String content) {
        messageId++;
        Message msg = new Message();

        msg.setId(messageId);
        msg.setContent(content);

        return msg.getId();


    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        else{
            List<User> users = groupUserMap.get(group);
            boolean flag = false;
            for(User user : users){
                if(user == sender){
                    flag = true;
                }
            }
            if(flag == false){
                throw new Exception("You are not allowed to send message");
            }
            else{
                return message.getId();
            }
        }
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        else{
            String admin = adminMap.get(group).getName();
            if(!admin.equals(approver)){
                throw new Exception("Approver does not have rights");
            }
            else{
                List<User> users = groupUserMap.get(group);
                boolean flage = false;
                for(User ui : users){
                    if(ui.equals(user)){
                        flage = true;
                    }
                }
                if(flage == false){
                    throw new Exception("User is not a participant");
                }
                else{
                    User newUser = adminMap.get(group);
                    adminMap.remove(group);
                    adminMap.put(group,user);
                    List<User> newList = groupUserMap.get(group);
                    newList.add(newUser);
                    groupUserMap.put(group,newList);
                    return "SUCCESS";
                }

            }
        }
    }

    public int removeUser(User user) {
        return 0;

    }

    public String findMessage(Date start, Date end, int k) {
        return null;
    }


}
