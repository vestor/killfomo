package com.killfomo.web.rest;

import com.killfomo.KillfomoApp;

import com.killfomo.domain.AuthResource;
import com.killfomo.repository.AuthResourceRepository;
import com.killfomo.service.AuthResourceService;
import com.killfomo.service.dto.AuthResourceDTO;
import com.killfomo.service.mapper.AuthResourceMapper;
import com.killfomo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.killfomo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.killfomo.domain.enumeration.TaskType;
/**
 * Test class for the AuthResourceResource REST controller.
 *
 * @see AuthResourceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = KillfomoApp.class)
public class AuthResourceResourceIntTest {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final TaskType DEFAULT_TYPE = TaskType.DEFAULT;
    private static final TaskType UPDATED_TYPE = TaskType.FRESHDESK;

    @Autowired
    private AuthResourceRepository authResourceRepository;

    @Autowired
    private AuthResourceMapper authResourceMapper;

    @Autowired
    private AuthResourceService authResourceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAuthResourceMockMvc;

    private AuthResource authResource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AuthResourceResource authResourceResource = new AuthResourceResource(authResourceService);
        this.restAuthResourceMockMvc = MockMvcBuilders.standaloneSetup(authResourceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuthResource createEntity(EntityManager em) {
        AuthResource authResource = new AuthResource()
            .userId(DEFAULT_USER_ID)
            .token(DEFAULT_TOKEN)
            .type(DEFAULT_TYPE);
        return authResource;
    }

    @Before
    public void initTest() {
        authResource = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthResource() throws Exception {
        int databaseSizeBeforeCreate = authResourceRepository.findAll().size();

        // Create the AuthResource
        AuthResourceDTO authResourceDTO = authResourceMapper.toDto(authResource);
        restAuthResourceMockMvc.perform(post("/api/auth-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authResourceDTO)))
            .andExpect(status().isCreated());

        // Validate the AuthResource in the database
        List<AuthResource> authResourceList = authResourceRepository.findAll();
        assertThat(authResourceList).hasSize(databaseSizeBeforeCreate + 1);
        AuthResource testAuthResource = authResourceList.get(authResourceList.size() - 1);
        assertThat(testAuthResource.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAuthResource.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testAuthResource.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createAuthResourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authResourceRepository.findAll().size();

        // Create the AuthResource with an existing ID
        authResource.setId(1L);
        AuthResourceDTO authResourceDTO = authResourceMapper.toDto(authResource);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthResourceMockMvc.perform(post("/api/auth-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authResourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AuthResource in the database
        List<AuthResource> authResourceList = authResourceRepository.findAll();
        assertThat(authResourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = authResourceRepository.findAll().size();
        // set the field null
        authResource.setUserId(null);

        // Create the AuthResource, which fails.
        AuthResourceDTO authResourceDTO = authResourceMapper.toDto(authResource);

        restAuthResourceMockMvc.perform(post("/api/auth-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authResourceDTO)))
            .andExpect(status().isBadRequest());

        List<AuthResource> authResourceList = authResourceRepository.findAll();
        assertThat(authResourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = authResourceRepository.findAll().size();
        // set the field null
        authResource.setType(null);

        // Create the AuthResource, which fails.
        AuthResourceDTO authResourceDTO = authResourceMapper.toDto(authResource);

        restAuthResourceMockMvc.perform(post("/api/auth-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authResourceDTO)))
            .andExpect(status().isBadRequest());

        List<AuthResource> authResourceList = authResourceRepository.findAll();
        assertThat(authResourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuthResources() throws Exception {
        // Initialize the database
        authResourceRepository.saveAndFlush(authResource);

        // Get all the authResourceList
        restAuthResourceMockMvc.perform(get("/api/auth-resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getAuthResource() throws Exception {
        // Initialize the database
        authResourceRepository.saveAndFlush(authResource);

        // Get the authResource
        restAuthResourceMockMvc.perform(get("/api/auth-resources/{id}", authResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(authResource.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuthResource() throws Exception {
        // Get the authResource
        restAuthResourceMockMvc.perform(get("/api/auth-resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthResource() throws Exception {
        // Initialize the database
        authResourceRepository.saveAndFlush(authResource);
        int databaseSizeBeforeUpdate = authResourceRepository.findAll().size();

        // Update the authResource
        AuthResource updatedAuthResource = authResourceRepository.findOne(authResource.getId());
        // Disconnect from session so that the updates on updatedAuthResource are not directly saved in db
        em.detach(updatedAuthResource);
        updatedAuthResource
            .userId(UPDATED_USER_ID)
            .token(UPDATED_TOKEN)
            .type(UPDATED_TYPE);
        AuthResourceDTO authResourceDTO = authResourceMapper.toDto(updatedAuthResource);

        restAuthResourceMockMvc.perform(put("/api/auth-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authResourceDTO)))
            .andExpect(status().isOk());

        // Validate the AuthResource in the database
        List<AuthResource> authResourceList = authResourceRepository.findAll();
        assertThat(authResourceList).hasSize(databaseSizeBeforeUpdate);
        AuthResource testAuthResource = authResourceList.get(authResourceList.size() - 1);
        assertThat(testAuthResource.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAuthResource.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testAuthResource.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthResource() throws Exception {
        int databaseSizeBeforeUpdate = authResourceRepository.findAll().size();

        // Create the AuthResource
        AuthResourceDTO authResourceDTO = authResourceMapper.toDto(authResource);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAuthResourceMockMvc.perform(put("/api/auth-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authResourceDTO)))
            .andExpect(status().isCreated());

        // Validate the AuthResource in the database
        List<AuthResource> authResourceList = authResourceRepository.findAll();
        assertThat(authResourceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAuthResource() throws Exception {
        // Initialize the database
        authResourceRepository.saveAndFlush(authResource);
        int databaseSizeBeforeDelete = authResourceRepository.findAll().size();

        // Get the authResource
        restAuthResourceMockMvc.perform(delete("/api/auth-resources/{id}", authResource.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AuthResource> authResourceList = authResourceRepository.findAll();
        assertThat(authResourceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthResource.class);
        AuthResource authResource1 = new AuthResource();
        authResource1.setId(1L);
        AuthResource authResource2 = new AuthResource();
        authResource2.setId(authResource1.getId());
        assertThat(authResource1).isEqualTo(authResource2);
        authResource2.setId(2L);
        assertThat(authResource1).isNotEqualTo(authResource2);
        authResource1.setId(null);
        assertThat(authResource1).isNotEqualTo(authResource2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthResourceDTO.class);
        AuthResourceDTO authResourceDTO1 = new AuthResourceDTO();
        authResourceDTO1.setId(1L);
        AuthResourceDTO authResourceDTO2 = new AuthResourceDTO();
        assertThat(authResourceDTO1).isNotEqualTo(authResourceDTO2);
        authResourceDTO2.setId(authResourceDTO1.getId());
        assertThat(authResourceDTO1).isEqualTo(authResourceDTO2);
        authResourceDTO2.setId(2L);
        assertThat(authResourceDTO1).isNotEqualTo(authResourceDTO2);
        authResourceDTO1.setId(null);
        assertThat(authResourceDTO1).isNotEqualTo(authResourceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(authResourceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(authResourceMapper.fromId(null)).isNull();
    }
}
