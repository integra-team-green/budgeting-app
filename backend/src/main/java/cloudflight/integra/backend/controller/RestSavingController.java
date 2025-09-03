package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.mapper.ISavingMapper;
import cloudflight.integra.backend.service.ISavingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/savings")
@Tag(name = "saving", description = "All about your savings")
public class RestSavingController {

    private final ISavingService savingService;
    private final ISavingMapper savingMapper;

    @Autowired
    public RestSavingController(ISavingService savingService, ISavingMapper savingMapper) {
        this.savingService = savingService;
        this.savingMapper = savingMapper;
    }


    @Operation(summary = "Get saving by ID", description = "Returns a single saving ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saving found"),
            @ApiResponse(responseCode = "404", description = "Saving not found")
    })
    @GetMapping(value = "/{savingId}")
    public ResponseEntity<?> getSavingById(
            @Parameter(description = "ID of saving to return") @PathVariable Long savingId) {
        System.out.println("GET /savings/" + savingId + " called, searching for saving with ID: " + savingId);

        try {
            Saving saving = savingService.getSavingById(savingId);
            SavingDTO savingDTO = savingMapper.toDTO(saving);
            return ResponseEntity.ok(savingDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Saving with ID " + savingId + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    @Operation(summary = "Get all savings", description = "Returns all available savings")
    @GetMapping()
    public ResponseEntity<?> getAllSavings() {

        System.out.println("GET /savings called, returning all savings.");

        try {
            Iterable<Saving> savings = savingService.getAllSavings();
            Iterable<SavingDTO> savingDTOS = savingMapper.toDtoList(savings);
            return ResponseEntity.ok(savingDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    @Operation(summary = "Add a new saving")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saving added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping()
    public ResponseEntity<?> addSaving(
            @RequestBody(description = "Saving to add") @org.springframework.web.bind.annotation.RequestBody SavingDTO savingDTO) {
        System.out.println("POST /savings called, adding new saving: " + savingDTO);

        try {
            Saving saving = savingMapper.toEntity(savingDTO);
            savingService.addSaving(saving);
            System.out.println("Saving added successfully: " + saving);
            return ResponseEntity.ok(savingDTO);
        } catch (IllegalArgumentException | ValidationException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    @Operation(summary = "Update an existing saving by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saving updated"),
            @ApiResponse(responseCode = "400", description = "ID mismatch"),
            @ApiResponse(responseCode = "404", description = "Saving not found")
    })
    @PutMapping("/{savingId}")
    public ResponseEntity<?> updateSaving(
            @Parameter(description = "ID of saving to update")
            @PathVariable Long savingId,
            @RequestBody(description = "Updated saving") @org.springframework.web.bind.annotation.RequestBody SavingDTO savingDTO) {
        System.out.println("PUT /savings/" + savingId + " called, updating saving with ID: " + savingId);

        try {
            if (!savingId.equals(savingDTO.getId())) {
                return ResponseEntity.status(400).body("ID in path and request body do not match.");
            }

            Saving saving = savingMapper.toEntity(savingDTO);
            savingService.updateSaving(saving);
            System.out.println("Saving updated successfully: " + saving);
            return ResponseEntity.ok(savingDTO);
        } catch (ValidationException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    @Operation(summary = "Delete a saving by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saving updated"),
            @ApiResponse(responseCode = "404", description = "Saving not found")
    })
    @DeleteMapping("/{savingId}")
    public ResponseEntity<?> deleteSaving(
            @Parameter(description = "ID of saving to delete")@PathVariable Long savingId) {
        System.out.println("DELETE /savings/" + savingId + " called, deleting saving with ID: " + savingId);

        Saving saving = savingService.getSavingById(savingId);
        SavingDTO savingDTO = savingMapper.toDTO(saving);
        if (savingDTO == null) {
            return ResponseEntity.status(404).body("Saving with ID " + savingId + " not found.");
        }
        try {
            savingService.deleteSaving(savingId);
            System.out.println("Saving deleted successfully with ID: " + savingId);
            return ResponseEntity.ok(savingDTO);
        } catch (Exception e) {
            System.err.println("Error deleting saving with ID " + savingId + ": " + e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
