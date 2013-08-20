/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package machine;

import java.util.TimerTask;

/**
 *
 * @author jsanchez
 */
public class SpectrumTimer extends TimerTask {

    private Spectrum spectrum;
    private long lastTick;

    public SpectrumTimer(Spectrum spectrum) {
        this.spectrum = spectrum;
    }

    @Override
    public synchronized void run() {

        // El timer solo nos sirve de metr�nomo, pero es indiferente lo que se
        // retrase la se�al, ya que el tiempo lo marca la tarjeta de sonido.
        // Lo que s� hay que hacer es purgar todos los eventos demasiado
        // retrasados para que no se acumulen en el sistema.
//        long now = System.currentTimeMillis();
//        System.out.println("Tick delayed: " + (now - scheduledExecutionTime()) + " at frame " + Clock.getInstance().getFrames());
        if (System.currentTimeMillis() - scheduledExecutionTime() < 100)
            spectrum.clockTick();
    }
}
