# Simple EMV Simulator

This directory contains the Windows simulator from `dtksimpleemvdemo`, with the
network path standardized as follows:

`Test client 127.0.0.1:5556 -> adb reverse -> PC simulator :5557`

## Start

1. Connect the POS device and confirm that ADB reports it as `device`.
2. Run `start_emv_simulator.bat`. For a specific device, pass its serial as the
   first argument, for example: `start_emv_simulator.bat 0123456789ABCDEF`.
3. In the Simulator window, select TCP/IP mode, set the listening port to
   `5557`, and start listening.
4. In the test client, connect Device Service and open **Simple EMV Demo**.
5. The App defaults are already `127.0.0.1`, port `5556`, and amount `12345`.
   SET/CLEAR AID and RID, Set Keys, Sign In, Balance, Purchase, PinPad, and the
   custom PIN screen remain available for direct manual testing.

## Stop

Run `stop_emv_simulator.bat`, optionally with the same device serial. It closes
the simulator process and removes the reverse mapping for device port `5556`.

## Use an already running simulator

If another compatible simulator is already listening on the PC, keep the App
defaults and map device port `5556` to that simulator's port. For example, for
a simulator listening on PC port `5555`:

`adb -s <device-serial> reverse tcp:5556 tcp:5555`

This only creates the network mapping; it does not start or stop the existing
simulator process.
