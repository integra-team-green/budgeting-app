package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.IncomeDTO;
import cloudflight.integra.backend.entity.Income;
import cloudflight.integra.backend.entity.User;
import cloudflight.integra.backend.entity.validation.IncomeValidator;
import cloudflight.integra.backend.exception.NotFoundException;
import cloudflight.integra.backend.mapper.IncomeMapper;
import cloudflight.integra.backend.repository.IncomeRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.IncomeService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IncomeServiceImpl implements IncomeService {

  private final IncomeRepository incomeRepo;
  private final IncomeValidator incomeValidator;
  private final UserRepository userRepo;

  public IncomeServiceImpl(
      IncomeRepository incomeRepo, IncomeValidator incomeValidator, UserRepository userRepo) {
    this.incomeRepo = incomeRepo;
    this.incomeValidator = incomeValidator;
    this.userRepo = userRepo;
  }

  @Override
  @Transactional
  public IncomeDTO createIncome(IncomeDTO incomeDTO) {
    Income income = IncomeMapper.toEntity(incomeDTO);
    incomeValidator.validate(income);
    User user = userRepo.findById(incomeDTO.getUserId()).orElse(null);
    income.setUser(user);
    return IncomeMapper.toDTO(incomeRepo.save(income));
  }

  @Override
  @Transactional(readOnly = true)
  public Iterable<IncomeDTO> getAllIncomes() {
    List<IncomeDTO> dtos = new ArrayList<>();
    for (Income income : incomeRepo.findAll()) {
      dtos.add(IncomeMapper.toDTO(income));
    }

    return dtos;
  }

  @Override
  @Transactional(readOnly = true)
  public IncomeDTO getIncomeById(Long id) {
    if (id == null) throw new IllegalArgumentException("Income id must not be null.");
    return IncomeMapper.toDTO(
        incomeRepo.findById(id).orElseThrow(() -> new NotFoundException("Income not found")));
  }

  @Override
  @Transactional
  public IncomeDTO updateIncome(IncomeDTO incomeDTO) {
    Income income = IncomeMapper.toEntity(incomeDTO);
    incomeValidator.validate(income);
    if (incomeRepo.findById(income.getId()).isEmpty()) {
      throw new NotFoundException("Income with id " + income.getId() + " not found for update");
    }
    User user = userRepo.findById(incomeDTO.getUserId()).orElse(null);
    income.setUser(user);

    return IncomeMapper.toDTO(incomeRepo.save(income));
  }

  @Override
  @Transactional
  public void deleteIncome(Long id) {
    if (incomeRepo.findById(id).isEmpty()) {
      throw new NotFoundException("Income with id " + id + " not found for delete");
    }
    incomeRepo.deleteById(id);
  }
}
