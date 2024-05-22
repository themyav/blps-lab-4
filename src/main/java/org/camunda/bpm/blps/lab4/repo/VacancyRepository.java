package org.camunda.bpm.blps.lab4.repo;

import org.camunda.bpm.blps.lab4.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}

