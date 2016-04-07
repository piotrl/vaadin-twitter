package net.piotrl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Long> {
    List<Party> findByNameStartsWithIgnoreCase(String partyName);
}
