package cloudflight.integra.backend.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cloudflight.integra.backend.dto.UserDTO;
import cloudflight.integra.backend.dto.auth.AuthenticationRequest;
import cloudflight.integra.backend.dto.auth.AuthenticationResponse;
import cloudflight.integra.backend.dto.auth.RegisterRequest;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

    private String testToken;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String aliceEmail;

    private String marcEmail;

    @BeforeEach
    void setUp() throws Exception {

        aliceEmail = "alice+" + UUID.randomUUID() + "@email.com";
        userRepo.save(new User(null, "Alice", aliceEmail, "123"));
        marcEmail = "marc+" + UUID.randomUUID() + "@email.com";
        userRepo.save(new User(null, "Marc", marcEmail, "abcd999"));
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        String email = "test+" + UUID.randomUUID() + "@example.com";
        registerRequest.setEmail(email);
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        testToken =
                objectMapper.readValue(response, AuthenticationResponse.class).getToken();
    }

    @Test
    void testGetUserById() throws Exception {
        User alice = userRepo.findByEmail(aliceEmail).orElseThrow();
        Long aliceId = alice.getId();

        mockMvc.perform(get("/api/v1/users/" + aliceId).header("Authorization", "Bearer " + testToken))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value(aliceEmail));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/999").header("Authorization", "Bearer " + testToken))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddUser() throws Exception {
        UserDTO userDto = new UserDTO();
        userDto.setName("Bob");
        userDto.setEmail("bob@email.com");
        userDto.setPassword("passw");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                        .header("Authorization", "Bearer " + testToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@email.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        User alice = userRepo.findByEmail(aliceEmail).orElseThrow();
        Long aliceId = alice.getId();

        UserDTO updateDto = new UserDTO();
        updateDto.setId(aliceId);
        updateDto.setName("Alice Updated");
        updateDto.setEmail("alice@new.com");
        updateDto.setPassword("newpass");

        mockMvc.perform(put("/api/v1/users/" + aliceId)
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser() throws Exception {
        User alice = userRepo.findByEmail(aliceEmail).orElseThrow();
        Long aliceId = alice.getId();

        mockMvc.perform(delete("/api/v1/users/" + aliceId).header("Authorization", "Bearer " + testToken))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/users/" + aliceId).header("Authorization", "Bearer " + testToken))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users").header("Authorization", "Bearer " + testToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetUserByEmail() throws Exception {
        mockMvc.perform(get("/api/v1/users/by-email")
                        .header("Authorization", "Bearer " + testToken)
                        .param("email", aliceEmail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value(aliceEmail));
    }
}
