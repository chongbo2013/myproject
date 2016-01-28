/*
 * Copyright (c) 2015 银河互联网电视有限公司. All rights reserved.
 */

package com.yeyanxiang.util.gitv;

import de.greenrobot.event.EventBus;

public final class EventBusHelper {

    private static EventBus sEventBus;

    static {
        // FIXME config default EventBus
        sEventBus = EventBus.builder().installDefaultEventBus();
    }

    private EventBusHelper() {
        // no instances
        throw new UnsupportedOperationException();
    }

    public static void post(Object event) {
        sEventBus.post(event);
    }

    public static void postSticky(Object event) {
        sEventBus.postSticky(event);
    }

    public static void register(Object subscriber) {
        sEventBus.register(subscriber);
    }

    public static void register(Object subscriber, int priority) {
        sEventBus.register(subscriber, priority);
    }

    public static void registerSticky(Object subscriber) {
        sEventBus.registerSticky(subscriber);
    }

    public static void registerSticky(Object subscriber, int priority) {
        sEventBus.registerSticky(subscriber, priority);
    }

    public static void unregister(Object subscriber) {
        sEventBus.unregister(subscriber);
    }

    public static boolean removeStickyEvent(Object event) {
        return sEventBus.removeStickyEvent(event);
    }

    public static <T> T removeStickyEvent(Class<T> eventType) {
        return sEventBus.removeStickyEvent(eventType);
    }

    public static void removeAllStickyEvents() {
        sEventBus.removeAllStickyEvents();
    }


    public static void cancelEventDelivery(Object event) {
        sEventBus.cancelEventDelivery(event);
    }

    public static void clearCaches() {
        sEventBus.clearCaches();
    }

    public static <T> T getStickyEvent(Class<T> eventType) {
        return sEventBus.getStickyEvent(eventType);
    }

    public static boolean hasSubscriberForEvent(Class<?> eventClass) {
        return sEventBus.hasSubscriberForEvent(eventClass);
    }

    public static boolean isRegistered(Object subscriber) {
        return sEventBus.isRegistered(subscriber);
    }

}
