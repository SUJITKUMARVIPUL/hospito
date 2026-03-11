package com.hospito.entity;

import com.hospito.entity.num.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import static com.hospito.entity.num.RoomStatus.AVAILABLE;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomNumber;
    private String roomType;
    private Double pricePerNight;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;
}
