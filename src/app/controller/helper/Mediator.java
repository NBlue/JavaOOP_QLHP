package app.controller.helper;

import javafx.event.ActionEvent;
import java.util.*;

import javafx.event.Event;
import javafx.event.EventHandler;

public class Mediator {

    private static HashMap<String, List<EventHandler<ActionEvent>>> action_dict =
            new HashMap<String, List<EventHandler<ActionEvent>>>();

    public static void unSubscribe(String token, EventHandler<ActionEvent> callBack) {
        if (action_dict.containsKey(token))
            action_dict.get(token).remove(callBack);
    }

    public static void unSubscribe(String token) {
        if (action_dict.containsKey(token))
            action_dict.remove(token);
    }

    public static void subscribe(String token, EventHandler<ActionEvent> callBack) {
        if (!action_dict.containsKey(token)) {
            var list = new ArrayList<EventHandler<ActionEvent>>();
            list.add(callBack);
            action_dict.put(token, list);
        }
        else
        {
            boolean found = false;
            for (var item : action_dict.get(token)) {
                if (item.toString().equals(callBack.toString()))
                    found = true;
            }
            if (!found)
                action_dict.get(token).add(callBack);
        }
    }

    public static void Notify(String token)
    {
        Notify(token, null);
    }

    public static void Notify(String token, ActionEvent obj)
    {
        if (action_dict.containsKey(token))
            for (EventHandler<ActionEvent> callBack : action_dict.get(token))
                callBack.handle(obj);
    }
}
