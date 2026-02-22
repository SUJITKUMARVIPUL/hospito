package com.hospito.service;

import com.hospito.entity.Guest;
import com.hospito.exception.HospitoException;
import com.hospito.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;

    public Guest registerGuest(Guest guest){
        if(guestRepository.existsByEmail(guest.getEmail())){
            throw new HospitoException(guest.getEmail()+" already registered", HttpStatus.CONFLICT);
        }
        if(guestRepository.existsByPhoneNumber(guest.getPhoneNumber())){
            throw new HospitoException(guest.getPhoneNumber()+" already registered",HttpStatus.CONFLICT );
        }
        return guestRepository.save(guest);
    }

    public List<Guest> getAllGuests(){
        return guestRepository.findAll();
    }

    public Guest getGuestById(Long guestId){
        return guestRepository.findById(guestId).orElseThrow(
                ()->new HospitoException("Guest not found",HttpStatus.NOT_FOUND)
        );
    }

    public Guest findGuestByEmailOrPhoneNumber(String emailOrPhoneNumber){
        return guestRepository.findByEmail(emailOrPhoneNumber)
                .or(()->guestRepository.findByPhoneNumber(emailOrPhoneNumber))
                .orElseThrow(
                        ()->new HospitoException("Guest not found with "+emailOrPhoneNumber,HttpStatus.NOT_FOUND)
                );
    }

    public Guest updateGuest(Long guestId,Guest guestDetails){
        Guest existingGuest = getGuestById(guestId);
        if(guestDetails.getFirstName() != null){
            existingGuest.setFirstName(guestDetails.getFirstName());
        }
        if(guestDetails.getLastName() != null){
            existingGuest.setLastName(guestDetails.getLastName());
        }
        if(guestDetails.getEmail() != null){
            existingGuest.setEmail(guestDetails.getEmail());
        }
        if(guestDetails.getPhoneNumber() != null){
            existingGuest.setPhoneNumber(guestDetails.getPhoneNumber());
        }
        if(guestDetails.getPassword() != null){
            existingGuest.setPassword(guestDetails.getPassword());
        }
        if(guestDetails.getIdProofNumber() != null){
            existingGuest.setIdProofNumber(guestDetails.getIdProofNumber());
        }
        return guestRepository.save(existingGuest);
    }

    public void deleteGuestById(Long guestId){
        if(!guestRepository.existsById(guestId)){
            throw new HospitoException("Guest not found!",HttpStatus.NOT_FOUND);
        }
        guestRepository.deleteById(guestId);
    }
}
