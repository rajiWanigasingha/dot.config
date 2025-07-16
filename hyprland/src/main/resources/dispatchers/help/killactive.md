### Close Active Window (killactive)

closes (not kills) the active window

##### Close vs Kill

**Close**

The killactive command sends a request to the application asking it to gracefully close itself, similar to clicking the close button on a window.
This allows the app to perform cleanup tasks like saving data or asking for confirmation before quitting.

**Kill**

Forcefully terminates the application process using a signal like SIGKILL, giving the app no chance to clean up.
This can result in data loss or corruption if the application was performing tasks (e.g., editing a file or downloading).