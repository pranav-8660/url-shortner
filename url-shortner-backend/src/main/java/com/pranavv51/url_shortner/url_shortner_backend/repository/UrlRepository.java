package com.pranavv51.url_shortner.url_shortner_backend.repository;

import com.pranavv51.url_shortner.url_shortner_backend.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UrlRepository extends JpaRepository<Url, UUID> {
    public Url findByUrlId(UUID urlId);
}
