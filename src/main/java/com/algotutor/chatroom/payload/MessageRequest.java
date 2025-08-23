package com.algotutor.chatroom.payload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

	private String content;
	
	private String roomId;
	
	private String sender;
	
}
