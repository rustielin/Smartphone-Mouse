# Smartphone-Mouse
## "aka Chaotic Evil" -- Joe

This is a WIP. Android phone is currently broken, and has been for a while. Expecting to work on this project more in the future. 
Idea is to lay your smartphone face up on a desk and slide it around as you would a traditional mouse. Click the on-screen buttons for left, right, middle click, etc. and vibration feedback will confirm action. Add support for more advanced gestures too such as pinch zoom and possibly macros. 

### Procedure
Currently sends linear accelerometer sensor data to server on PC via UDP. Server side processing extrapolates on-screen mouse position and velocity. Working to develop a more reliable way to transform the accelerometer data. 
