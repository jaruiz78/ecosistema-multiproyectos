# 🥋 Kata 12: Arquitectura Zero-Trust BeyondCorp, OIDC y Patrón Backend-For-Frontend (BFF)

---

## 🏛️ 1. Ancla Mental y Analogía Isomórfica (El Test de los 12 Años)

> **Analogía**: Imagina una embajada de máxima seguridad.
> - **El modelo antiguo (Perímetro Tradicional)**: Pones un muro gigante afuera. Quien logre saltar el muro, puede caminar libremente por todos los despachos sin mostrar identificación. Si un espía se disfraza de jardinero, tiene acceso total.
> - **El modelo Zero-Trust (BeyondCorp)**: No confías en el muro. Cada puerta dentro de la embajada exige un escáner biométrico, validación de la tarjeta con firma criptográfica y verificación en tiempo real de que la persona tiene autorización expresa para entrar a esa habitación específica en ese instante. Nunca confíes, siempre verifica.

---

## 🔬 2. Primeros Principios: BeyondCorp y Aislamiento Celular

1. **Sin Red Privada Confiada**: La ubicación de la red (LAN vs WAN) no otorga privilegios. Todo tráfico es cifrado en tránsito con mTLS (TLS 1.3).
2. **Context-Aware Access**: Las decisiones de autorización evalúan la identidad del usuario (JWT/JWKS), el estado del dispositivo y los claims de tenant.
3. **Patrón Backend-For-Frontend (BFF)**: Las cookies de sesión son `HttpOnly`, `Secure` y `SameSite=Strict`. El frontend nunca almacena tokens JWT en `localStorage` o `sessionStorage`, evitando ataques XSS.

---

## 💻 3. Implementación en Java 25 / Spring Security 7

```java
package com.corp.security.kata;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ZeroTrustSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Protegido por tokens criptográficos en el BFF
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/public/**", "/actuator/health").permitAll()
                .requestMatchers("/api/v1/tenants/{tenantId}/**").access((authentication, context) -> {
                    String requestedTenant = context.getVariables().get("tenantId");
                    boolean matchesTenant = authentication.get().getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("TENANT_" + requestedTenant) || a.getAuthority().equals("ROLE_SUPERADMIN"));
                    return new org.springframework.security.authorization.AuthorizationDecision(matchesTenant);
                })
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            .build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        authoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}
```

---

## 🧪 4. Ejercicio de Verificación Hermética

Escribe una prueba unitaria con `@WebMvcTest` y `SecurityMockMvcRequestPostProcessors.jwt()` verificando que un usuario del Tenant `tenant-almeria` sea rechazado con HTTP `403 Forbidden` si intenta consultar `/api/v1/tenants/tenant-sevilla/datos`.

---

## 🧠 5. Desafío Feynman y Rúbrica de Auto-Evaluación

> **Reto Feynman**: ¿Cómo le explicarías a un niño de 12 años por qué un hotel nunca debe darle a un huésped la llave de la habitación de otro, aunque ambos huéspedes sean personas educadas y muestren un carnet de identidad real?

### Rúbrica de Evaluación:
1. **Nivel 1 (Básico)**: Explica la diferencia entre autenticación (quién eres) y autorización celular multi-tenant (a qué habitación o datos tienes permiso de acceder).
2. **Nivel 2 (Intermedio)**: Detalla el flujo del patrón BFF (*Backend for Frontend*): la cookie segura HttpOnly/SameSite en el navegador del usuario, el token criptográfico OIDC/JWT interno y la propagación de claims (`tenant_id`, `roles`) hacia los microservicios con validación en cada frontera.
3. **Nivel 3 (Ph.D. / Staff)**: Formaliza el modelo Zero-Trust de NIST SP 800-207, el aislamiento de datos a nivel de fila (*Row-Level Security* / RLS), la prevención de vulnerabilidades BOLA/IDOR (OWASP API Security) y el diseño de políticas inmutables en tiempo de compilación con pruebas herméticas.

