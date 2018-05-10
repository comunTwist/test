package com.gmail.agentup.repository;

import com.gmail.agentup.model.Entry;
import com.gmail.agentup.model.EntryType;
import com.gmail.agentup.model.UserOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    @Query("SELECT u FROM Entry u WHERE u.type = :type AND u.userOwner = :userOwner")
    Entry findUserEntryByType(@Param("type") EntryType type, @Param("userOwner")UserOwner userOwner);

    @Query("SELECT e FROM Entry e WHERE e.userOwner.id = :id")
    List<Entry> findAllUserEntries(@Param("id") Long id);
}
