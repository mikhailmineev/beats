package ru.mikhailmineev.beats.controller;

public interface Controller {

    void start();

    void stop();

    void increaseBPM();

    void decreaseBPM();

    void setBPM(int bpm);
}
