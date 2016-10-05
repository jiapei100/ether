package org.corebounce.ui;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.widgets.Display;

import ch.fhnw.ether.platform.Platform;
import ch.fhnw.util.IDisposable;

public class Repeating extends TimerTask implements IDisposable {
	private static final Timer timer = new Timer(true);
	
	final AtomicReference<Runnable> r;
	
	public Repeating(int initialDelay, int delay, Runnable r) {
		timer.schedule(this, initialDelay, delay);
		this.r     = new AtomicReference<>(r);
		Platform.get().addShutdownDispose(this);
	}
	
	@Override
	public void run() {
		Runnable r = this.r.get();
		if(r == null) stop();
		Display.getDefault().asyncExec(r);
	}

	public synchronized void stop() {
		r.set(null);
		cancel();
		Platform.get().removeShutdownTask(this);
	}

	@Override
	public void dispose() {
		stop();
	}
}
