//package ru.nsu.rabetskii.xmlmessage;
//
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//import java.util.List;
//
//@XmlRootElement(name = "success")
//public class Success {
//    @XmlElement(name = "users")
//    private Users users;
//
//    public Success() {}
//
//    public Success(Users users) {
//        this.users = users;
//    }
//
//    public Users getUsers() {
//        return users;
//    }
//
//    public static class Users {
//        @XmlElement(name = "user")
//        private List<User> userList;
//
//        public Users() {}
//
//        public Users(List<User> userList) {
//            this.userList = userList;
//        }
//
//        public List<User> getUserList() {
//            return userList;
//        }
//    }
//
//    public static class User {
//        @XmlElement(name = "name")
//        private String name;
//
//        public User() {}
//
//        public User(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return name;
//        }
//    }
//}
