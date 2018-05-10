package com.gmail.agentup.services;

import com.gmail.agentup.model.Entry;
import com.gmail.agentup.model.EntryType;
import com.gmail.agentup.model.UserOwner;
import com.gmail.agentup.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EntryServiceImpl implements EntryService {
    @Autowired
    private EntryRepository entryRepository;

    @Override
    @Transactional(readOnly = true)
    public Entry getEntryByType(EntryType type, UserOwner userOwner) {
        return entryRepository.findUserEntryByType(type, userOwner);
    }

    @Override
    @Transactional(readOnly = true)
    public Entry getEntryById(Long id) {
        return entryRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entry> getAllUserEntries(Long userId) {
        return entryRepository.findAllUserEntries(userId);
    }

    @Override
    @Transactional
    public void addEntry(Entry entry) {
        entryRepository.save(entry);
    }

    @Override
    @Transactional
    public void updateEntry(Entry entry) {
        entryRepository.save(entry);
    }

    @Override
    @Transactional
    public void deleteEntry(Entry entry) {
        entryRepository.delete(entry);
    }
}
