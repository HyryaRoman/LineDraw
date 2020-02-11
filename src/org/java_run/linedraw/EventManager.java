package org.java_run.linedraw;

import java.util.ArrayList;
import java.util.List;

public class EventManager {
    //list of listeners
    private List<EventListener> listeners;

    //on create
    public EventManager(){
        listeners=new ArrayList<>();
    }

    //Add listener
    //Returns true if action is successfully
    public boolean addListener(EventListener listener){
        try {
            this.listeners.add(listener);
            return true;
        }catch (Exception e){
            e.fillInStackTrace();
            return false;
        }
    }

    //Removes listener
    //Returns true if action is successfully
    public boolean removeListener(EventListener listener){
        try {
            this.listeners.remove(listener);
            return true;
        }catch (Exception e){
            e.fillInStackTrace();
            return false;
        }
    }

    //notify about something
    public void notify(String eventName,Object object){
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).event(eventName, object);
        }
    }
}
