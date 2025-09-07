package cloudflight.integra.backend.service;


import cloudflight.integra.backend.entity.Credit;

import java.util.List;

public interface CreditService {

    Credit create(Credit credit);

    Credit findById(Long id);

    List<Credit> findAll();

    Credit update(Long id, Credit credit);

    void delete(Long id);
}