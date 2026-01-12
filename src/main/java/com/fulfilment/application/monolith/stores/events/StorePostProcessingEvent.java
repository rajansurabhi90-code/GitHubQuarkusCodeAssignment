package com.fulfilment.application.monolith.stores.events;

import com.fulfilment.application.monolith.stores.Store;

public class StorePostProcessingEvent {
    public enum EventName {
        CREATE,
        UPDATE,
        PATCH,
        DELETE
    }
    private final Store store;
    private final EventName eventName;

    public StorePostProcessingEvent(Store store, EventName eventName) {
        this.store = store;
        this.eventName = eventName;
    }

    public Store getStore() {
        return store;
    }

    public EventName getEventName() {
        return eventName;
    }
}
