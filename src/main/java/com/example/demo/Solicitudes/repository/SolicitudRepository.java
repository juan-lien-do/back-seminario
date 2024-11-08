package com.example.demo.Solicitudes.repository;

import com.example.demo.Solicitudes.domain.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
}
