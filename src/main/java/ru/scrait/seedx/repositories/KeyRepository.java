package ru.scrait.seedx.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.scrait.seedx.models.Key;

@Repository
public interface KeyRepository extends JpaRepository<Key, String> {

}