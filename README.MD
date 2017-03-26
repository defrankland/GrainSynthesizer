#GrainSynthesizer

See http://www.defcodeprojects.com for more details

##Platform:
Windows

##Description:
Runs grain synthesis on a wav file to create a "stretching" effect on the playback of the selected portion of the file.

This is a simple "granular" (or "grain") synthesizer project in Java and C++.

I created this project while reviewing techniques for simulating car engine sounds in racing video games.
It wouldn't create a realistic effect to simply play back sound files, rather the engine sound needs to react immediately to changes in user input (e.g. as soon as the "throttle" button is released, the engine sound should react by decreasing in pitch like a real car would). I came across a few projects using the grain synthesizer approach and created this code to test it out.

The sliders correspond to a grain and adjust the sample position in the file. The grain length and other parameters can be adjusted to maximize the fun!
