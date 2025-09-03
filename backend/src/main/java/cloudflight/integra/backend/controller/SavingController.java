package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.controller.problem.SavingApiErrorResponses;
import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.MoneyMindRuntimeException;
import cloudflight.integra.backend.mapper.SavingMapper;
import cloudflight.integra.backend.service.ISavingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/savings")
@SavingApiErrorResponses
@Tag(name = "saving", description = "All about your savings")
public class SavingController {

    private static final Logger log = LoggerFactory.getLogger(SavingController.class);
    private final ISavingService savingService;

    @Autowired
    public SavingController(ISavingService savingService) {
        this.savingService = savingService;
    }


    @Operation(summary = "Get saving by ID", description = "Returns a single saving")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saving found"),
            @ApiResponse(responseCode = "404", description = "Saving not found")
    })
    @GetMapping(value = "/{savingId}")
    public ResponseEntity<?> getSavingById(
            @Parameter(description = "ID of saving to return") @PathVariable Long savingId) {
        log.info("GET /savings/" + savingId + " called, searching for saving with ID: " + savingId);

        try {
            Saving saving = savingService.getSavingById(savingId);
            SavingDTO savingDTO = SavingMapper.toDTO(saving);
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

        log.info("GET /savings called, returning all savings.");

        try {
            Iterable<Saving> savings = savingService.getAllSavings();
            Iterable<SavingDTO> savingDTOS = SavingMapper.toDtoList(savings);
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
            @RequestBody(
                    description = "Saving to add",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SavingDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "SavingExample",
                                            value = "{ \"amount\": 10000.00, \"date\": \"2025-09-04T10:47:00+00:00\", \"goal\": \"Apartament\", \"description\": \"My dream apartament\" }"
                                    ),
                                    @ExampleObject(
                                            name = "BadExample",
                                            value = "{ \"amount\": -2000.00, \"date\": \"2222-09-04T10:47:00+00:00\", \"goal\": \"\", \"description\": \"bad description\" }"
                                    )
                            }
                    )

            ) @org.springframework.web.bind.annotation.RequestBody SavingDTO savingDTO) {
        log.info("POST /savings called, adding new saving: " + savingDTO);

        try {
            Saving saving = SavingMapper.toEntity(savingDTO);
            savingService.addSaving(saving);
            log.info("Saving added successfully: " + saving);
            return ResponseEntity.ok(savingDTO);
        } catch (IllegalArgumentException | ValidationException | MoneyMindRuntimeException e) {
            log.error("Error adding saving: " + e.getMessage());
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error adding saving: " + e.getMessage());
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
        log.info("PUT /savings/" + savingId + " called, updating saving with ID: " + savingId);

        try {
            if (!savingId.equals(savingDTO.getId())) {
                return ResponseEntity.status(400).body("ID in path and request body do not match.");
            }

            Saving saving = SavingMapper.toEntity(savingDTO);
            savingService.updateSaving(saving);
            log.info("Saving updated successfully: " + saving);
            return ResponseEntity.ok(savingDTO);
        } catch (ValidationException e) {
            log.error("Validation error updating saving with ID " + savingId + ": " + e.getMessage());
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error updating saving with ID " + savingId + ": " + e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    @Operation(summary = "Delete a saving by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Saving deleted"),
            @ApiResponse(responseCode = "404", description = "Saving not found")
    })
    @DeleteMapping("/{savingId}")
    public ResponseEntity<?> deleteSaving(
            @Parameter(description = "ID of saving to delete")@PathVariable Long savingId) {
        log.info("DELETE /savings/" + savingId + " called, deleting saving with ID: " + savingId);

        try {
            savingService.deleteSaving(savingId);
            log.info("Saving deleted successfully with ID: " + savingId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            log.error("Error deleting saving with ID " + savingId + ": " + e.getMessage());
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
