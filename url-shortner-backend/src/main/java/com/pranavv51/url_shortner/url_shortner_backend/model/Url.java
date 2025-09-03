package com.pranavv51.url_shortner.url_shortner_backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "Url")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID urlId;

    private StringBuffer originalUrl;
    private Date createdAt;
    private Date expiryTime;
    private boolean isExpired;

    private static Date calculateExpiryTime(){
        LocalDateTime expiryDateTime = LocalDateTime.now().plusDays(7);
        return Date.from(expiryDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Url(StringBuffer longUrl){
        this.originalUrl = longUrl;
        this.createdAt = new Date();
        this.expiryTime = calculateExpiryTime();
        this.isExpired = false;
    }

}
