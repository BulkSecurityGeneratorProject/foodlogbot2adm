package com.foodlog.foodlog.processor.prox;

import com.foodlog.foodlog.processor.Processor;
import org.springframework.stereotype.Component;

/**
 * Created by rafael on 27/10/17.
 */
@Component
public class ProxProcessor extends Processor {

    @Override
    public void process() {
        System.out.println("Process " + this.getClass().getName());
        sendMessage("Process " + this.getClass().getName());
    }

    @Override
    public boolean check() {
        return update.getMessage().getText() != null && update.getMessage().getText().trim().toLowerCase().equals("prox");
    }
}
