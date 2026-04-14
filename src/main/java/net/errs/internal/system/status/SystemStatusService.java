package net.errs.internal.system.status;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import net.errs.internal.system.status.dto.SystemStatusDto;
import org.springframework.stereotype.Service;

@Service
public class SystemStatusService {

    private final MeterRegistry meterRegistry;

    public SystemStatusService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public SystemStatusDto getStatus() {
        SystemStatusDto dto = new SystemStatusDto();

        double systemCpu = gauge("system.cpu.usage") * 100.0;
        double processCpu = gauge("process.cpu.usage") * 100.0;

        double memTotal = gauge("system.memory.total");
        double memAvailable = gauge("system.memory.available");
        double jvmHeapUsed = gauge("jvm.memory.used", "area", "heap");
        double jvmHeapMax = gauge("jvm.memory.max", "area", "heap");
        double diskFree = gauge("disk.free");
        double diskTotal = gauge("disk.total");
        double liveThreads = gauge("jvm.threads.live");
        double uptime = gauge("process.uptime");

        dto.setSystemCpuUsagePercent(round2(systemCpu));
        dto.setProcessCpuUsagePercent(round2(processCpu));

        long memTotalMb = bytesToMb(memTotal);
        long memAvailableMb = bytesToMb(memAvailable);
        dto.setSystemMemoryAvailableMb(memAvailableMb);
        dto.setSystemMemoryUsedMb(Math.max(memTotalMb - memAvailableMb, 0));

        dto.setJvmHeapUsedMb(bytesToMb(jvmHeapUsed));
        dto.setJvmHeapMaxMb(bytesToMb(jvmHeapMax));

        dto.setDiskFreeGb(bytesToGb(diskFree));
        dto.setDiskTotalGb(bytesToGb(diskTotal));

        dto.setLiveThreads((int) Math.round(liveThreads));
        dto.setUptimeSeconds((long) Math.round(uptime));

        return dto;
    }

    private double gauge(String name, String... tags) {
        Gauge gauge = meterRegistry.find(name).tags(tags).gauge();
        if (gauge == null || gauge.value() < 0 || Double.isNaN(gauge.value())) {
            return 0.0;
        }
        return gauge.value();
    }

    private long bytesToMb(double bytes) {
        return (long) (bytes / 1024.0 / 1024.0);
    }

    private long bytesToGb(double bytes) {
        return (long) (bytes / 1024.0 / 1024.0 / 1024.0);
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}