package ru.mikhailmineev.beats.model;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class BeatModelImpl implements BeatModel, MetaEventListener {

	private static final int DEFAULT_BPM = 1;

	Sequencer sequencer;

	Sequence sequence;

	Track track;

	List<BeatObserver> beatObersvers = new ArrayList<>();

	List<BPMObserver> bpmObersvers = new ArrayList<>();

	int bpm = DEFAULT_BPM;

	@Override
    public void initialize() {
		setUpMidi();
		buildTrackAndStart();
	}

	@Override
    public void on() {
		sequencer.start();
		setBPM(DEFAULT_BPM);
	}

	@Override
    public void off() {
		setBPM(0);
		sequencer.stop();
	}

	@Override
    public void setBPM(int bpm) {
		this.bpm = bpm;
		sequencer.setTempoInBPM(getBPM());
		notifyBpmObservers();
	}

	@Override
    public int getBPM() {
		return bpm;
	}

	private void beatEvent() {
		notifyBeatObservers();
	}

	@Override
    public void registerObserver(BeatObserver o) {
		beatObersvers.add(o);

	}

	@Override
    public void deregisterObserver(BeatObserver o) {
		beatObersvers.remove(o);

	}

	public void notifyBeatObservers() {
		for (BeatObserver o : beatObersvers) {
			o.updateBeat();
		}
	}

	@Override
    public void registerObserver(BPMObserver o) {
		bpmObersvers.add(o);

	}

	@Override
    public void deregisterObserver(BPMObserver o) {
		bpmObersvers.remove(o);

	}

	public void notifyBpmObservers() {
		for (BPMObserver o : bpmObersvers) {
			o.updateBPM();
		}
	}

	@Override
	public void meta(MetaMessage meta) {
		if (meta.getType() == 47) {
			beatEvent();
			sequencer.start();
			setBPM(getBPM());
		}
	}

	public void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.addMetaEventListener(this);
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(getBPM());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buildTrackAndStart() {
		int[] trackList = { 35, 0, 46, 0 };

		sequence.deleteTrack(null);
		track = sequence.createTrack();

		makeTracks(trackList);
		track.add(makeEvent(192, 9, 1, 0, 4));

		try {
			sequencer.setSequence(sequence);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void makeTracks(int[] list) {
		for (int i = 0; i < list.length; i++) {
			int key = list[i];
			if (key != 0) {
				track.add(makeEvent(144, 9, key, 100, i));
				track.add(makeEvent(128, 9, key, 100, i + 1));
			}
		}
	}

	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return event;
	}
}
