package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.controller.Paths.GET_BY_ID_PATH;
import static ru.yandex.practicum.filmorate.controller.Paths.MPA_PATH;
import static ru.yandex.practicum.filmorate.messages.TechnicalMessages.RECEIVED_GET;

@Slf4j
@RestController
@RequestMapping(MPA_PATH)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MPAController {

    private final MPAService mpaService;

    @GetMapping()
    public Collection<MPA> getAll() {
        log.info(RECEIVED_GET + MPA_PATH);
        return mpaService.getAllMpa();
    }

    @GetMapping(GET_BY_ID_PATH)
    public MPA getById(@PathVariable(value = "id") int id) {
        log.info(RECEIVED_GET + MPA_PATH + id);
        return mpaService.getMpaById(id);
    }
}