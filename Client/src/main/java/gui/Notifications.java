package gui;

import javafx.application.Platform;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
/**
 * A Notifications class - mediator for observer pattern. Subscribers use a String as an event identifier they want to subscribe to.
 * Publisher publishes event by specifying the event identifier.
 * That way subscribers and publishers don't need to store each other's references and directly contact each other
 */
public class Notifications {
    public final static String SCENE_CHANGE_EVENT = "SCENE_CHANGE_EVENT";
    public final static String ACCOUNT_CHANGE_EVENT = "ACCOUNT_CHANGE_EVENT";

    private static final Map<String, List<SubscriberObject>> subscribers = new LinkedHashMap<>();

    public static void publish(String event) {

        Platform.runLater(() -> {
            List<SubscriberObject> subscriberList = subscribers.get(event);

            if (subscriberList != null) {

                subscriberList.forEach(
                        subscriberObject -> subscriberObject.getAction().accept(event)
                );
            }
        });
    }

    public static void subscribe(String event, Object subscriber, Consumer<String> action) {

        if (!subscribers.containsKey(event)) {
            List<SubscriberObject> slist = new ArrayList<>();
            subscribers.put(event, slist);
        }

        List<SubscriberObject> subscriberList = subscribers.get(event);

        subscriberList.add(new SubscriberObject(subscriber, action));
    }

    public static void unsubscribe(String event, Object subscriber) {

        List<SubscriberObject> subscriberList = subscribers.get(event);

        if (subscriberList != null) {
            subscriberList.remove(subscriber);
        }
    }

    static class SubscriberObject {
        @Getter
        private final Object subscriber;
        @Getter
        private final Consumer<String> action;

        public SubscriberObject(Object subscriber,
                                Consumer<String> action) {
            this.subscriber = subscriber;
            this.action = action;
        }

        @Override
        public int hashCode() {
            return subscriber.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SubscriberObject that = (SubscriberObject) o;
            return subscriber.equals(that.subscriber) && action.equals(that.action);
        }
    }
}
