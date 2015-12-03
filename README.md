# SoundCheckAndroid

This project is a showcase app for me to research audio recognition (Shazam like inspired)

It allows to record wav samples (display its waveform, frequency spectrum and spectrogram) and compare it afterwards with newly registered audio patterns.

The app talks with a backend where all the business logic happens (Fast Fourier Transform and Spectrogram comparison) See: https://github.com/ctarabusi/SoundCheckServer

The rendering is made trough Custom Views and RxJava is used to handle background processing.

The workflow should be:

- Record an audio pattern (a part of a song or a sound)
- Optionally show Waveform, Frequency and Spectrogram
- Compare it with a portion of that song: the result is a number indicating how many consecutives spectrogram matches were found

Work In Progress
