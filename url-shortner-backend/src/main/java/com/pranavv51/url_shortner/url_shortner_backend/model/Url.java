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

    // I have planned to set the expiry using a trigger or a cron(say at 6am everyday)..this happens separately outside the application
    // In a meantime, i can also delete the expired entries (say 2 days after expiry)...this can also be a cron(crontab running a bash script)/trigger
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

    public UUID getUrlId(){
        return this.urlId;
    }

    public boolean getIsExpired(){
        return this.isExpired;
    }

    public StringBuffer getOriginalUrl(){
        return this.originalUrl;
    }

}