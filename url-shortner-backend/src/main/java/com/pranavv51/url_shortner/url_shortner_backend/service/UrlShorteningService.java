package com.pranavv51.url_shortner.url_shortner_backend.service;

import com.pranavv51.url_shortner.url_shortner_backend.model.Url;
import com.pranavv51.url_shortner.url_shortner_backend.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UrlShorteningService {

    private final UrlRepository urlRepository;
    private final RedisTemplate<String,String> redisTemplate;

    @Value("${machineHostName}")
    private String machineDNS;

    @Value("${enabledProtocol}")
    private String protocol;

    /*private final String baseUrl = protocol+"://"+machineDNS+":8555/";*/

    private final String baseUrl = "http://localhost:8555/";

    public UrlShorteningService(UrlRepository urlRepository, RedisTemplate<String, String> redisTemplate) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
    }

    private void saveUrlMappingToRedisForCaching(String urlId,String longUrl){
        redisTemplate.opsForValue().set(urlId,longUrl,2, TimeUnit.DAYS); //TTL is 2 days
    }

    private UUID saveUrlToDB(String longUrl){
        Url savedUrl = urlRepository.save(new Url(longUrl));
        saveUrlMappingToRedisForCaching(savedUrl.getUrlId().toString(),longUrl);
        return savedUrl.getUrlId();
    }

    public String shortenUrl(String longUrl){
        UUID savedUrl = saveUrlToDB(longUrl);
        return baseUrl+savedUrl.toString();
    }

    private boolean isUrlExpired(UUID urlId){
        return urlRepository.findByUrlId(urlId).getIsExpired();
    }

    public String retrieveOrigUrl(UUID urlId){

        //first check if the url is cached inside redis...if not retrieve it from the db
        String cachedUrl = redisTemplate.opsForValue().get(urlId.toString());

        //if found in the cache, return it...
        if(cachedUrl!=null) return cachedUrl;

        //if not in cache, then we need to fall back to the db
        Url url = urlRepository.findByUrlId(urlId);

        if(url==null || url.getIsExpired()){
            return null;
        }

        //cache it into the db now, as it is accessed
        saveUrlMappingToRedisForCaching(urlId.toString(),url.getOriginalUrl());

        return url.getOriginalUrl();

    }
}