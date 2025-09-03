package com.pranavv51.url_shortner.url_shortner_backend.service;

import com.pranavv51.url_shortner.url_shortner_backend.model.Url;
import com.pranavv51.url_shortner.url_shortner_backend.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlShorteningService {

    private final UrlRepository urlRepository;

    @Value("${machineHostName}")
    private String machineDNS;

    @Value("${enabledProtocol}")
    private String protocol;

    private final String baseUrl = protocol+"://"+machineDNS+"/";

    public UrlShorteningService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
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
