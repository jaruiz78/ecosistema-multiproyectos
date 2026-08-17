package com.corp.proyectosalud.application;

import com.corp.proyectosalud.domain.model.ClinicalTrialSample;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de Custodia y Cumplimiento de Ensayos Clínicos con Privacidad Zero-PII.
 *
 * <p>Valida la cadena de frío biológica y genera sellos inmutables SHA-256 en \(O(1)\)
 * sin riesgo de Carrier Thread Pinning bajo Virtual Threads.
 *
 * @see docs/formacion_ecosistema/modulo_6_ciberseguridad_criptografia/01_zero_trust_y_pqc.md
 */
@Service
public class ClinicalTrialComplianceLedgerService {

    private final ReentrantLock lock = new ReentrantLock();

    public record CustodySeal(
            String sampleId,
            boolean coldChainMaintained,
            String zeroPiiSubjectDigest,
            String custodySealHash,
            long timestampMs
    ) {}

    public CustodySeal sealSampleCustody(
            ClinicalTrialSample sample,
            double recordedTemperatureCelsius,
            double minAllowedTemp,
            double maxAllowedTemp,
            String rawPatientId
    ) {
        Objects.requireNonNull(sample, "sample no puede ser nulo");
        Objects.requireNonNull(rawPatientId, "rawPatientId no puede ser nulo");

        lock.lock();
        try {
            boolean coldChainOk = recordedTemperatureCelsius >= minAllowedTemp
                    && recordedTemperatureCelsius <= maxAllowedTemp;

            String piiDigest = computeSha256("PATIENT_SALT:" + rawPatientId);
            String custodySeal = computeSha256(sample.id() + ":" + piiDigest + ":" + recordedTemperatureCelsius + ":" + coldChainOk);

            return new CustodySeal(
                    sample.id(),
                    coldChainOk,
                    piiDigest,
                    custodySeal,
                    System.currentTimeMillis()
            );
        } finally {
            lock.unlock();
        }
    }

    private static String computeSha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}
