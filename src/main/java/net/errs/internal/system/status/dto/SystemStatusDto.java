package net.errs.internal.system.status.dto;

public class SystemStatusDto {
    private double systemCpuUsagePercent;
    private double processCpuUsagePercent;
    private long systemMemoryUsedMb;
    private long systemMemoryAvailableMb;
    private long jvmHeapUsedMb;
    private long jvmHeapMaxMb;
    private long diskFreeGb;
    private long diskTotalGb;
    private int liveThreads;
    private long uptimeSeconds;

    public double getSystemCpuUsagePercent() {
        return systemCpuUsagePercent;
    }

    public void setSystemCpuUsagePercent(double systemCpuUsagePercent) {
        this.systemCpuUsagePercent = systemCpuUsagePercent;
    }

    public double getProcessCpuUsagePercent() {
        return processCpuUsagePercent;
    }

    public void setProcessCpuUsagePercent(double processCpuUsagePercent) {
        this.processCpuUsagePercent = processCpuUsagePercent;
    }

    public long getSystemMemoryUsedMb() {
        return systemMemoryUsedMb;
    }

    public void setSystemMemoryUsedMb(long systemMemoryUsedMb) {
        this.systemMemoryUsedMb = systemMemoryUsedMb;
    }

    public long getSystemMemoryAvailableMb() {
        return systemMemoryAvailableMb;
    }

    public void setSystemMemoryAvailableMb(long systemMemoryAvailableMb) {
        this.systemMemoryAvailableMb = systemMemoryAvailableMb;
    }

    public long getJvmHeapUsedMb() {
        return jvmHeapUsedMb;
    }

    public void setJvmHeapUsedMb(long jvmHeapUsedMb) {
        this.jvmHeapUsedMb = jvmHeapUsedMb;
    }

    public long getJvmHeapMaxMb() {
        return jvmHeapMaxMb;
    }

    public void setJvmHeapMaxMb(long jvmHeapMaxMb) {
        this.jvmHeapMaxMb = jvmHeapMaxMb;
    }

    public long getDiskFreeGb() {
        return diskFreeGb;
    }

    public void setDiskFreeGb(long diskFreeGb) {
        this.diskFreeGb = diskFreeGb;
    }

    public long getDiskTotalGb() {
        return diskTotalGb;
    }

    public void setDiskTotalGb(long diskTotalGb) {
        this.diskTotalGb = diskTotalGb;
    }

    public int getLiveThreads() {
        return liveThreads;
    }

    public void setLiveThreads(int liveThreads) {
        this.liveThreads = liveThreads;
    }

    public long getUptimeSeconds() {
        return uptimeSeconds;
    }

    public void setUptimeSeconds(long uptimeSeconds) {
        this.uptimeSeconds = uptimeSeconds;
    }
}
