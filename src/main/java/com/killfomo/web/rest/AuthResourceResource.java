package com.killfomo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.killfomo.service.AuthResourceService;
import com.killfomo.web.rest.errors.BadRequestAlertException;
import com.killfomo.web.rest.util.HeaderUtil;
import com.killfomo.service.dto.AuthResourceDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AuthResource.
 */
@RestController
@RequestMapping("/api")
public class AuthResourceResource {

    private final Logger log = LoggerFactory.getLogger(AuthResourceResource.class);

    private static final String ENTITY_NAME = "authResource";

    private final AuthResourceService authResourceService;

    public AuthResourceResource(AuthResourceService authResourceService) {
        this.authResourceService = authResourceService;
    }

    /**
     * POST  /auth-resources : Create a new authResource.
     *
     * @param authResourceDTO the authResourceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new authResourceDTO, or with status 400 (Bad Request) if the authResource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/auth-resources")
    @Timed
    public ResponseEntity<AuthResourceDTO> createAuthResource(@Valid @RequestBody AuthResourceDTO authResourceDTO) throws URISyntaxException {
        log.debug("REST request to save AuthResource : {}", authResourceDTO);
        if (authResourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new authResource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthResourceDTO result = authResourceService.save(authResourceDTO);
        return ResponseEntity.created(new URI("/api/auth-resources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /auth-resources : Updates an existing authResource.
     *
     * @param authResourceDTO the authResourceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated authResourceDTO,
     * or with status 400 (Bad Request) if the authResourceDTO is not valid,
     * or with status 500 (Internal Server Error) if the authResourceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/auth-resources")
    @Timed
    public ResponseEntity<AuthResourceDTO> updateAuthResource(@Valid @RequestBody AuthResourceDTO authResourceDTO) throws URISyntaxException {
        log.debug("REST request to update AuthResource : {}", authResourceDTO);
        if (authResourceDTO.getId() == null) {
            return createAuthResource(authResourceDTO);
        }
        AuthResourceDTO result = authResourceService.save(authResourceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, authResourceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /auth-resources : get all the authResources.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of authResources in body
     */
    @GetMapping("/auth-resources")
    @Timed
    public List<AuthResourceDTO> getAllAuthResources() {
        log.debug("REST request to get all AuthResources");
        return authResourceService.findAll();
        }

    /**
     * GET  /auth-resources/:id : get the "id" authResource.
     *
     * @param id the id of the authResourceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the authResourceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/auth-resources/{id}")
    @Timed
    public ResponseEntity<AuthResourceDTO> getAuthResource(@PathVariable Long id) {
        log.debug("REST request to get AuthResource : {}", id);
        AuthResourceDTO authResourceDTO = authResourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(authResourceDTO));
    }

    /**
     * DELETE  /auth-resources/:id : delete the "id" authResource.
     *
     * @param id the id of the authResourceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/auth-resources/{id}")
    @Timed
    public ResponseEntity<Void> deleteAuthResource(@PathVariable Long id) {
        log.debug("REST request to delete AuthResource : {}", id);
        authResourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
