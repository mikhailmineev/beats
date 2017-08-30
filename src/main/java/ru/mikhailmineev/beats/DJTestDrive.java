package ru.mikhailmineev.beats;

import ru.mikhailmineev.beats.controller.BeatController;
import ru.mikhailmineev.beats.controller.Controller;
import ru.mikhailmineev.beats.model.BeatModel;
import ru.mikhailmineev.beats.model.BeatModelImpl;

public class DJTestDrive {

    public static void main (String[] args) {
        BeatModel model = new BeatModelImpl();
        Controller controller = new BeatController(model);
    }
}

