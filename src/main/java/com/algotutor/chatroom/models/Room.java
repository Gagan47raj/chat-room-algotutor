package com.algotutor.chatroom.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "rooms")
public class Room {
 
	@Id
	private String id;
	
	private String roomId;
	private List<Message> messages = new ArrayList<>();
	
}
