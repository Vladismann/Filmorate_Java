package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MPADbStorage;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.GET_MPA;

@Service
@Slf4j
public class MPAService {

    MPADbStorage mpaDbStorage;

    @Autowired
    MPAService(MPADbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Collection<MPA> getAllMpa() {
        log.info(GET_MPA, mpaDbStorage.getAllMPA());
        return mpaDbStorage.getAllMPA();
    }

    public MPA getMpaById(int id) {
        return mpaDbStorage.getMPAById(id);
    }

}