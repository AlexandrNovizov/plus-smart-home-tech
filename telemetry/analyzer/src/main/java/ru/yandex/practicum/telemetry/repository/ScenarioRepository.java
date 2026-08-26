package ru.yandex.practicum.telemetry.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.telemetry.model.Scenario;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    @EntityGraph(attributePaths = {"conditions"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Scenario> findByHubId(String hubId);
    @EntityGraph(attributePaths = {"actions"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Scenario> findByIdIn(Collection<Long> ids);
    @EntityGraph(attributePaths = {"conditions", "actions"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Scenario> findByHubIdAndName(String hubId, String name);
    void deleteByHubIdAndName(String hubId, String name);
}
