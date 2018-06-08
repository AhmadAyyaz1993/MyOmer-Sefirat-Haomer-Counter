package com.sefirah.myomer.event_bus;

/**
 * Created by ahmad on a3/11/18.
 */

public class Events {


    // Event used to send message from fragment to activity.
    public static class FragmentActivityMessage {
        private String message;
        public FragmentActivityMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to fragment.
    public static class DayChangeEvent {
        private int day = 1;

        public DayChangeEvent(int day) {
            this.day = day;
        }

        public int getDayChangeEvent() {
            return day;
        }
    }

    // Event used to send message from activity to activity.
    public static class ActivityActivityMessage {
        private String message;
        public ActivityActivityMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

}
