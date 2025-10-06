package cloudflight.integra.backend.saving;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import cloudflight.integra.backend.dto.SavingDTO;
import cloudflight.integra.backend.entity.Saving;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.SavingValidator;
import cloudflight.integra.backend.entity.validation.ValidationException;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.mapper.SavingMapper;
import cloudflight.integra.backend.repository.SavingRepository;
import cloudflight.integra.backend.repository.UserRepository;

import cloudflight.integra.backend.service.impl.SavingServiceImpl;
import java.math.BigDecimal;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class SavingServiceTest {
  @Mock
  private SavingRepository savingRepository;

  @Mock
  private SavingValidator savingValidator;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SavingMapper savingMapper;

  @InjectMocks
  private SavingServiceImpl savingService;

  private Saving saving;
  private SavingDTO savingDTO;
  private User testUser;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testUser = new User();
    testUser.setId(1L);

    saving = new Saving();
    saving.setId(1L);
    saving.setUser(testUser);
    saving.setAmount(BigDecimal.valueOf(5000));
    saving.setDate(new Date());
    saving.setGoal("First house");
    saving.setDescription("For my dream house");

    savingDTO = new SavingDTO();
    savingDTO.setId(1L);
    savingDTO.setUserId(1L);
    savingDTO.setAmount(BigDecimal.valueOf(5000));
    savingDTO.setDate(saving.getDate());
    savingDTO.setGoal("First house");
    savingDTO.setDescription("For my dream house");
  }

  @Test
  void testAddSaving() {
    doNothing().when(savingValidator).validate(any(Saving.class));
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(savingRepository.save(any(Saving.class))).thenAnswer(inv -> {
      Saving s = inv.getArgument(0);
      s.setId(1L);
      return s;
    });

    SavingDTO dto = new SavingDTO();
    dto.setUserId(1L);
    dto.setAmount(BigDecimal.valueOf(5000));
    dto.setDate(new Date());
    dto.setGoal("First house");
    dto.setDescription("For my dream house");

    SavingDTO created = savingService.addSaving(dto);

    assertThat(created).isNotNull();
    assertThat(created.getId()).isEqualTo(1L);
    assertThat(created.getGoal()).isEqualTo("First house");
    verify(savingRepository, times(1)).save(any(Saving.class));
    verify(userRepository, times(1)).findById(1L);
    verify(savingValidator, times(1)).validate(any(Saving.class));}

  @Test
  void testAddSaving_userNotFound() {
    doNothing().when(savingValidator).validate(saving);
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> savingService.addSaving(savingDTO));
    verify(savingRepository, never()).save(any());
  }

  @Test
  void testAddSaving_validationFails() {
    doThrow(new ValidationException("Amount must be positive")).when(savingValidator).validate(any(Saving.class));

    assertThrows(ValidationException.class, () -> savingService.addSaving(savingDTO));
    verify(savingRepository, never()).save(any());
  }

  @Test
  void testGetSavingById() {
    when(savingRepository.findById(1L)).thenReturn(Optional.of(saving));

    SavingDTO found = savingService.getSavingById(1L);

    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(1L);
    assertThat(found.getGoal()).isEqualTo("First house");
    verify(savingRepository, times(1)).findById(1L);
  }

  @Test
  void testGetSavingById_notFound() {
    when(savingRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> savingService.getSavingById(1L));
  }

  @Test
  void testUpdateSaving() {
    doNothing().when(savingValidator).validate(any(Saving.class));
    when(savingRepository.findById(saving.getId())).thenReturn(Optional.of(saving));
    when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    when(savingRepository.save(any(Saving.class))).thenAnswer(inv -> inv.getArgument(0));

    SavingDTO updated = savingService.updateSaving(savingDTO);

    assertThat(updated.getGoal()).isEqualTo("First house");
    verify(savingRepository).findById(1L);
    verify(userRepository).findById(1L);
    verify(savingValidator).validate(any(Saving.class));
    verify(savingRepository).save(any(Saving.class));
  }

  @Test
  void testUpdateSaving_notFound() {
    when(savingRepository.findById(saving.getId())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> savingService.updateSaving(savingDTO));
  }

  @Test
  void testDeleteSaving() {
    when(savingRepository.findById(1L)).thenReturn(Optional.of(saving));
    doNothing().when(savingRepository).deleteById(1L);

    savingService.deleteSaving(1L);

    verify(savingRepository, times(1)).deleteById(1L);
  }

  @Test
  void testDeleteSaving_notFound() {
    when(savingRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> savingService.deleteSaving(1L));
    verify(savingRepository, never()).deleteById(any());
  }

  @Test
  void testGetAllSavings() {
    when(savingRepository.findAll()).thenReturn(List.of(saving));

    List<SavingDTO> all = (List<SavingDTO>) savingService.getAllSavings();

    assertThat(all).hasSize(1);
    assertThat(all.get(0).getId()).isEqualTo(1L);
    assertThat(all.get(0).getGoal()).isEqualTo("First house");
    verify(savingRepository, times(1)).findAll();
  }
}
