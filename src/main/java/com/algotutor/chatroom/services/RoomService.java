package com.algotutor.chatroom.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algotutor.chatroom.models.Room;
import com.algotutor.chatroom.repo.RoomRepository;

@Service
public class RoomService {

	private final RoomRepository roomRepo;
	
	@Autowired
	public RoomService(RoomRepository roomRepo)
	{
		this.roomRepo = roomRepo;
	}
	
	public Room findByRoomId(String roomId)
	{
		return roomRepo.findByRoomId(roomId);
	}
	public Room saveRoom(Room room)
	{
		return roomRepo.save(room);
	}
	
}
