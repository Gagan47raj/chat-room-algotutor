package com.algotutor.chatroom.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.algotutor.chatroom.models.Message;
import com.algotutor.chatroom.models.Room;
import com.algotutor.chatroom.payload.MessageRequest;
import com.algotutor.chatroom.repo.RoomRepository;

@RestController
@CrossOrigin("http://localhost:3000")
public class MessageController {

	@Autowired
	private RoomRepository roomRepo;
	
	
	@MessageMapping("/sendMessage/{roomId}")
	@SendTo("/topic/room/{roomId}")
	public Message sendMessage(
			@RequestBody MessageRequest messageRequest,
			@DestinationVariable String roomId)
	{
		Room room = roomRepo.findByRoomId(messageRequest.getRoomId());
		
		Message message = new Message();
		message.setContent(messageRequest.getContent());
		message.setSender(messageRequest.getSender());
		message.setTimeStamp(LocalDateTime.now());
		
		if(room != null)
		{
			room.getMessages().add(message);
			roomRepo.save(room);
		}
		else
		{
			throw new RuntimeException("Room not found");
		}
		
		return message;
	}
}
