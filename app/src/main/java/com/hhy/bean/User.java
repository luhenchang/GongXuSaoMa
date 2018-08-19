package com.hhy.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by hhy on 15/9/26.
 */
public class User implements Serializable{

    /**
     * code : 1
     * username : 001
     * password : 001
     * wordname : [{"id":"AA","name":"订单已雕刻"},{"id":"CC","name":"订单打磨"}]
     * wordid : AA
     */

    private int code;
    private String username;
    private String password;
    private String wordid;
    private List<WordnameBean> wordname;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWordid() {
        return wordid;
    }

    public void setWordid(String wordid) {
        this.wordid = wordid;
    }

    public List<WordnameBean> getWordname() {
        return wordname;
    }

    public void setWordname(List<WordnameBean> wordname) {
        this.wordname = wordname;
    }

    public static class WordnameBean implements Serializable{
        /**
         * id : AA
         * name : 订单已雕刻
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "WordnameBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "code='" + code + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", wordid='" + wordid + '\'' +
                ", wordname=" + wordname +
                '}';
    }
}
