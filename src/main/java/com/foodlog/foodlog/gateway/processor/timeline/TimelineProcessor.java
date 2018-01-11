package com.foodlog.foodlog.gateway.processor.timeline;

import com.foodlog.foodlog.gateway.processor.Processor;
import com.foodlog.foodlog.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rafael on 27/10/17.
 */
@Component
public class TimelineProcessor extends Processor {

    @Autowired
    private Util util;

    @Override
    public void process() {
        sendMessage("Sua Timeline sera gerada...");

        try {
            URL url = new URL("https://foodlogbot2adm.herokuapp.com/foodlog/timeline/index.html?userid=" + getCurrentUser(update).getId());

            util.performHttpGet(url, getCurrentUser(update).getLogin());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean check() {
        try {
            return update.getMessage().getText().trim().toLowerCase().equals("timeline");
        } catch (Exception e) {
            return false;
        }
    }

    //para testes
    public void setUtil(Util util) {
        this.util = util;
    }
}
