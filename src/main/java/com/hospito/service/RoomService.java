package com.hospito.service;

import com.hospito.entity.Room;
import com.hospito.entity.num.RoomStatus;
import com.hospito.exception.HospitoException;
import com.hospito.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room createRoom(Room room) {
        if(roomRepository.findByRoomNumber(room.getRoomNumber()).isPresent()){
            throw new HospitoException("Room number" + room.getRoomNumber()+" already exists!", HttpStatus.CONFLICT);
        }
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

    public List<Room> getRoomsByStatus(RoomStatus status){
        return roomRepository.findByStatus(status);
    }

    public Room updateRoomStatus(Long roomId,RoomStatus status){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()->new HospitoException("Room number "+roomId+"does not exists!",HttpStatus.NOT_FOUND));
        room.setStatus(status);
        return roomRepository.save(room);
    }

    public void deleteRoom(Long roomId){
        if(!roomRepository.existsById(roomId)){
            throw new HospitoException("Room with id "+roomId+" does not exists!",HttpStatus.NOT_FOUND);
        }
        roomRepository.deleteById(roomId);
    }

}
