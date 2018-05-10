package com.gmail.agentup.services;

import com.gmail.agentup.model.Entry;
import com.gmail.agentup.model.EntryType;
import com.gmail.agentup.model.UserOwner;

import java.util.List;

public interface EntryService {
    Entry getEntryByType(EntryType type, UserOwner userOwner);

    Entry getEntryById(Long id);

    List<Entry> getAllUserEntries(Long userId);

    void addEntry(Entry entry);

    void updateEntry(Entry entry);

    void deleteEntry(Entry entry);
}
