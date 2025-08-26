package com.algotutor.chatroom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algotutor.chatroom.models.Message;
import com.algotutor.chatroom.models.Room;
import com.algotutor.chatroom.services.RoomService;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("http://localhost:5173")
public class RoomController {

	public final RoomService roomService;
	
	public RoomController(RoomService roomService)
	{
		this.roomService = roomService;
	}
	
	@PostMapping
	public ResponseEntity<?> createRoom(@RequestBody String roomId)
	{
	    roomId = roomId.replace("\"", "");

		if(roomService.findByRoomId(roomId) != null)
		{
			return ResponseEntity.badRequest().body("Room already exists");
		}
		Room room = new Room();
		room.setRoomId(roomId);
		Room savedRoom = roomService.saveRoom(room);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
	}
	
	@GetMapping("/{roomId}")
	public ResponseEntity<?> joinRoom(@PathVariable(name = "roomId") String roomId)
	{
		roomId = roomId.replace("\"", "");
		
		Room room = roomService.findByRoomId(roomId);
		if(room == null)
		{
			return ResponseEntity.badRequest().body("Room not found !");
		}
		return ResponseEntity.ok(room);
	}
	
	@GetMapping("/{roomId}/messages")
	public ResponseEntity<List<Message>> getMessage(
			@PathVariable(name="roomId") String roomId,
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "20", required = false) int size)
	{
		Room room = roomService.findByRoomId(roomId);
		if(room == null) return ResponseEntity.badRequest().build();
		
		List<Message> messages = room.getMessages();
		
		int start = Math.max(0, messages.size() - (page+1)*size);
		int end = Math.min(messages.size(), start + size);
		List<Message> sortedMessage = messages.subList(start, end);
		
		return ResponseEntity.ok(sortedMessage);
	}
	
	
}