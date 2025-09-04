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

    private final String baseUrl = protocol+"://"+machineDNS+":8555/";

    public UrlShorteningService(UrlRepository urlRepository, RedisTemplate<String, String> redisTemplate) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
    }

    private void saveUrlMappingToRedis(String urlId,String longUrl){
        redisTemplate.opsForValue().set(urlId,longUrl,2, TimeUnit.DAYS); //TTL is 2 days
    }

    private UUID saveUrlToDB(StringBuffer longUrl){
        Url savedUrl = urlRepository.save(new Url(longUrl));
        return savedUrl.getUrlId();
    }

    public StringBuffer shortenUrl(StringBuffer longUrl){
        UUID savedUrl = saveUrlToDB(longUrl);
        return new StringBuffer().append(baseUrl).append(savedUrl.toString());
    }

    private boolean isUrlExpired(UUID urlId){
        return urlRepository.findByUrlId(urlId).getIsExpired();
    }

    public StringBuffer retrieveOrigUrl(UUID urlId){
        Url url = urlRepository.findByUrlId(urlId);

        if(url==null || url.getIsExpired()){
            return null;
        }
        return url.getOriginalUrl();

    }
}
