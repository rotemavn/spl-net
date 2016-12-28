package bgu.spl.a2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {

    AtomicInteger currentVersion=new AtomicInteger(0);

    /**
     * The function returns the current version
     * @return the currrent version
     */
    public synchronized int getVersion() {
        return currentVersion.get();
    }

    /**
     * The function increments the current version and notifies all threads
     */
    public synchronized void inc() {
        currentVersion.getAndIncrement();
        notifyAll();

    }

    /**
     * As long as the version isn't modified, all threads must sleep
     * @param version a version to compare to
     * @throws InterruptedException when the version changes
     */
    public synchronized void await(int version) throws InterruptedException {
        while (currentVersion.get() == version) {
            wait();


        }
    }

}
