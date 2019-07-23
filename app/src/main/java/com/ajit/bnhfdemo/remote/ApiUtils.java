package com.ajit.bnhfdemo.remote;

public class ApiUtils {

    public static final String BASE_URL = "http://35.226.61.135:9090/rest/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}
