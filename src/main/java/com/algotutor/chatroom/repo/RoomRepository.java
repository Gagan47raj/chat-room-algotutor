package com.algotutor.chatroom.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.algotutor.chatroom.models.Room;

public interface RoomRepository extends MongoRepository<Room, String> {

	Room findByRoomId(String roomId);
	
}
