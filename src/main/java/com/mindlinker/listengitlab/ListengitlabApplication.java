package com.mindlinker.listengitlab;

import com.mindlinker.listengitlab.Listener.ServiceInfoListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class ListengitlabApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ListengitlabApplication.class, args);
        ServiceInfoListener serviceInfoListener = run.getBean(ServiceInfoListener.class);
        try {
            serviceInfoListener.listenGitlab();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
