package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.controller.problem.SavingApiErrorResponses;
import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.service.SavingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private final SavingService savingService;

    @Autowired
    public SavingController(SavingService savingService) {
        this.savingService = savingService;
    }

    @Operation(summary = "Get saving by ID", description = "Returns a single saving")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Saving found"),
                @ApiResponse(responseCode = "404", description = "Saving not found")
            })
    @GetMapping(value = "/{savingId}")
    public ResponseEntity<?> getSavingById(
            @Parameter(description = "ID of saving to return") @PathVariable Long savingId) {
        log.info("GET /savings/{} called, searching for saving with ID: {}", savingId, savingId);

        SavingDTO savingDTO = savingService.getSavingById(savingId);
        return ResponseEntity.ok(savingDTO);
    }

    @Operation(summary = "Get all savings", description = "Returns all available savings")
    @GetMapping()
    public ResponseEntity<?> getAllSavings() {

        log.info("GET /savings called, returning all savings.");
        Iterable<SavingDTO> savingDTOS = savingService.getAllSavings();
        return ResponseEntity.ok(savingDTOS);
    }

    @Operation(summary = "Add a new saving")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Saving added successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
            })
    @PostMapping()
    public ResponseEntity<?> addSaving(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Saving to add",
                            required = true,
                            content =
                                    @Content(
                                            schema = @Schema(implementation = SavingDTO.class),
                                            examples = {
                                                @ExampleObject(
                                                        name = "SavingExample",
                                                        value =
                                                                "{ \"amount\": 10000.00, \"date\": \"2025-09-04T10:47:00+00:00\", \"goal\": \"Apartament\", \"description\": \"My dream apartament\" }"),
                                                @ExampleObject(
                                                        name = "BadExample",
                                                        value =
                                                                "{ \"amount\": -2000.00, \"date\": \"2222-09-04T10:47:00+00:00\", \"goal\": \"\", \"description\": \"bad description\" }")
                                            }))
                    @RequestBody
                    SavingDTO savingDTO) {
        log.info("POST /savings called, adding new saving: {}", savingDTO);

        log.info("POST /savings called, adding new saving: {}", savingDTO);
        SavingDTO created = savingService.addSaving(savingDTO);
        log.info("Saving added successfully: {}", created);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update an existing saving by ID")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Saving updated"),
                @ApiResponse(responseCode = "400", description = "ID mismatch"),
                @ApiResponse(responseCode = "404", description = "Saving not found")
            })
    @PutMapping("/{savingId}")
    public ResponseEntity<?> updateSaving(
            @Parameter(description = "ID of saving to update") @PathVariable Long savingId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated saving")
                    @org.springframework.web.bind.annotation.RequestBody
                    SavingDTO savingDTO) {
        log.info("PUT /savings/{} called, updating saving with ID: {}", savingId, savingId);

        if (!savingId.equals(savingDTO.getId())) {
            throw new IllegalArgumentException("ID in path and request body do not match.");
        }
        SavingDTO updated = savingService.updateSaving(savingDTO);
        log.info("Saving updated successfully: {}", updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a saving by ID")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Saving deleted"),
                @ApiResponse(responseCode = "404", description = "Saving not found")
            })
    @DeleteMapping("/{savingId}")
    public ResponseEntity<?> deleteSaving(
            @Parameter(description = "ID of saving to delete") @PathVariable Long savingId) {
        log.info("DELETE /savings/{} called, deleting saving with ID: {}", savingId, savingId);

        savingService.deleteSaving(savingId);
        log.info("Saving deleted successfully with ID: {}", savingId);
        return ResponseEntity.noContent().build();
    }
}
