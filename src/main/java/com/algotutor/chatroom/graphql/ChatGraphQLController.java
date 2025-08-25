package com.algotutor.chatroom.graphql;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import com.algotutor.chatroom.models.Message;
import com.algotutor.chatroom.models.Room;
import com.algotutor.chatroom.services.RoomService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Controller
public class ChatGraphQLController {

    private final RoomService roomService;

    // Map of sinks for each room
    private final Map<String, Sinks.Many<Message>> roomSinks = new ConcurrentHashMap<>();

    public ChatGraphQLController(RoomService roomService) {
        this.roomService = roomService;
    }

    @QueryMapping
    public Room room(@Argument("roomId") String roomId) {
        return roomService.findByRoomId(roomId);
    }

    @MutationMapping
    public Room createRoom(@Argument("roomId") String roomId) {
        Room room = new Room();
        room.setRoomId(roomId);
        return roomService.saveRoom(room);
    }

    @MutationMapping
    public Message sendMessage(
    		@Argument("roomId") String roomId, 
    		@Argument("sender") String sender, 
    		@Argument("content") String content) {
        Room room = roomService.findByRoomId(roomId);
        if (room == null) throw new RuntimeException("Room not found!");

        Message message = new Message(sender, content, LocalDateTime.now());
        room.getMessages().add(message);
        roomService.saveRoom(room);

        // Publish to the correct room sink
        roomSinks.computeIfAbsent(roomId, id -> Sinks.many().multicast().onBackpressureBuffer())
                 .tryEmitNext(message);

        return message;
    }

    @SubscriptionMapping
    public Flux<Message> messageSent(@Argument("roomId") String roomId) {
        return roomSinks
                .computeIfAbsent(roomId, id -> Sinks.many().multicast().onBackpressureBuffer())
                .asFlux();
    }
}
