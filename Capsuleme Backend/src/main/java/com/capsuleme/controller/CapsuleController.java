package com.capsuleme.controller;

import com.capsuleme.dto.CapsuleDTO;
import com.capsuleme.dto.GrowthReportDTO;
import com.capsuleme.service.CapsuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/capsules")
@CrossOrigin(origins = "*")
public class CapsuleController {

    @Autowired
    private CapsuleService capsuleService;

    // POST /api/capsules — create a new capsule
    @PostMapping
    public ResponseEntity<Object> createCapsule(@RequestBody CapsuleDTO.CreateRequest request) {
        try {
            if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
                Map<String, String> err = new HashMap<>();
                err.put("error", "Name is required");
                return ResponseEntity.badRequest().body(err);
            }
            if (request.getUserEmail() == null || request.getUserEmail().trim().isEmpty()) {
                Map<String, String> err = new HashMap<>();
                err.put("error", "Email is required");
                return ResponseEntity.badRequest().body(err);
            }
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                Map<String, String> err = new HashMap<>();
                err.put("error", "Title is required");
                return ResponseEntity.badRequest().body(err);
            }
            if (request.getUnlockDate() == null) {
                Map<String, String> err = new HashMap<>();
                err.put("error", "Unlock date is required");
                return ResponseEntity.badRequest().body(err);
            }

            CapsuleDTO.CapsuleResponse response = capsuleService.createCapsule(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    // GET /api/capsules/by-email?email=... — get all capsules for an email
    @GetMapping("/by-email")
    public ResponseEntity<Object> getCapsulesByEmail(@RequestParam String email) {
        try {
            List<CapsuleDTO.CapsuleResponse> capsules = capsuleService.getCapsulesByEmail(email.trim());
            return ResponseEntity.ok(capsules);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    // GET /api/capsules/{id} — get one capsule
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCapsuleById(@PathVariable Long id) {
        try {
            java.util.Optional<CapsuleDTO.CapsuleResponse> result = capsuleService.getCapsuleById(id);
            if (result.isPresent()) {
                return ResponseEntity.ok(result.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    // POST /api/capsules/{id}/unlock — submit present snapshot and get growth report
    @PostMapping("/{id}/unlock")
    public ResponseEntity<Object> unlockCapsule(
            @PathVariable Long id,
            @RequestBody CapsuleDTO.UnlockRequest request) {
        try {
            GrowthReportDTO report = capsuleService.unlockCapsule(id, request);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
            }
            return ResponseEntity.badRequest().body(err);
        }
    }

    // GET /api/capsules/{id}/report — get growth report for already-unlocked capsule
    @GetMapping("/{id}/report")
    public ResponseEntity<Object> getGrowthReport(@PathVariable Long id) {
        try {
            GrowthReportDTO report = capsuleService.getGrowthReport(id);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
            }
            return ResponseEntity.badRequest().body(err);
        }
    }
}
