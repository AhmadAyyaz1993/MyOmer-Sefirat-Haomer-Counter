package com.sefirah.myomer.event_bus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ahmad on 3/11/18.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }

}
