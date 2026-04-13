package com.healthsync.controller;

import com.healthsync.entity.Pet;
import com.healthsync.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<Pet> getPet() {
        Pet pet = petService.getOrCreatePet();
        return ResponseEntity.ok(pet);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getPetInfo() {
        Map<String, Object> info = petService.getPetInfo();
        return ResponseEntity.ok(info);
    }

    @PostMapping("/revive")
    public ResponseEntity<Pet> revivePet() {
        Pet pet = petService.revivePet();
        return ResponseEntity.ok(pet);
    }
}
