package com.pranavv51.url_shortner.url_shortner_backend.controller;

import com.pranavv51.url_shortner.url_shortner_backend.DTO.InputLongUrl;
import com.pranavv51.url_shortner.url_shortner_backend.model.Url;
import com.pranavv51.url_shortner.url_shortner_backend.service.UrlShorteningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UrlShorteningController {

    private final UrlShorteningService urlShorteningService;

    public UrlShorteningController(UrlShorteningService urlShorteningService) {
        this.urlShorteningService = urlShorteningService;
    }


    @PostMapping(value="/shorten-url")
    public String shortenAndStoreUrl(@RequestBody InputLongUrl inputLongUrl){
        return urlShorteningService.shortenUrl(inputLongUrl.getInputLongUrl());
    }

    @GetMapping(value="/{urlId}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable("urlId") UUID urlId){

        String originalUrl = urlShorteningService.retrieveOrigUrl(urlId);

        if(originalUrl==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(302).header("Location",originalUrl.toString()).build();
    }
}
