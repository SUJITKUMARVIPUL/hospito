package com.hospito.controller;

import com.hospito.dto.ApiResponse;
import com.hospito.entity.Room;
import com.hospito.entity.num.RoomStatus;
import com.hospito.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/addRoom")
    public ResponseEntity<ApiResponse<Room>> addRoom(@RequestBody Room room){
        Room newRoom = roomService.createRoom(room);
        return ResponseEntity.ok(ApiResponse.success(newRoom,"Room created successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Room>>> getAllRooms(){
        return ResponseEntity.ok(ApiResponse.success(roomService.getAllRooms(),"Rooms retrieved"));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<List<Room>>> getRoomsByStatus(@RequestParam RoomStatus roomStatus){
        return ResponseEntity.ok(ApiResponse.success(roomService.getRoomsByStatus(roomStatus),"Rooms retrieved"));
    }

    @PatchMapping("/updateStatus/{roomId}")
    public ResponseEntity<ApiResponse<Room>> updateStatus(@PathVariable Long roomId,@RequestParam RoomStatus status){
        Room updated = roomService.updateRoomStatus(roomId, status);
        return ResponseEntity.ok(ApiResponse.success(updated,"Room status updated!"));
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<ApiResponse<String>> deleteRoom(@PathVariable Long roomId){

        return ResponseEntity.ok(ApiResponse.success(roomService.deleteRoom(roomId),"Room deleted successfully"));
    }
}
