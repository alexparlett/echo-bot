package org.homonoia.echo.web.controllers;

import org.homonoia.echo.model.RoomEnter;
import org.homonoia.echo.model.RoomExit;
import org.homonoia.echo.model.RoomMessage;
import org.homonoia.echo.model.RoomNotification;
import org.homonoia.echo.model.WebhookEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (c) 2015-2016 Homonoia Studios.
 *
 * @author alexparlett
 * @since 16/03/2017
 */
@RestController("/hipchat")
public class HipchatWebhookController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping(value = "/room-message", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleRoomMessage(@RequestBody WebhookEvent<RoomMessage> roomMessage) {
        applicationEventPublisher.publishEvent(roomMessage);
    }

    @PostMapping(value = "/room-notification", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleRoomNotification(@RequestBody WebhookEvent<RoomNotification> roomNotification) {
        applicationEventPublisher.publishEvent(roomNotification);
    }

    @PostMapping(value = "/room-enter", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleRoomEnter(@RequestBody WebhookEvent<RoomEnter> roomEnter) {
        applicationEventPublisher.publishEvent(roomEnter);
    }

    @PostMapping(value = "/room-exit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handleRoomExit(@RequestBody WebhookEvent<RoomExit> roomExit) {
        applicationEventPublisher.publishEvent(roomExit);
    }
}