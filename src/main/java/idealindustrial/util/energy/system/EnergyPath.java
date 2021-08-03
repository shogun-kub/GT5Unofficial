package idealindustrial.util.energy.system;

import java.util.ArrayList;
import java.util.List;

public class EnergyPath implements IEnergyPassThrough {

    protected NodeProducer from;
    protected NodeConsumer to;
    protected List<IEnergyPassThrough> passThroughList;
    protected long voltage, amperage, loss;

    public EnergyPath(List<IEnergyPassThrough> passThroughList) {
        this.passThroughList = passThroughList;
        this.amperage = passThroughList.stream().mapToLong(IEnergyPassThrough::maxAmperage).min().orElse(Long.MAX_VALUE);
        this.voltage = passThroughList.stream().mapToLong(IEnergyPassThrough::maxVoltage).min().orElse(Long.MAX_VALUE);
        this.loss = passThroughList.stream().mapToLong(IEnergyPassThrough::loss).sum();
    }

    public EnergyPath(EnergyPath energyPath) {
        this.passThroughList = new ArrayList<>(energyPath.passThroughList);
        this.voltage = energyPath.voltage;
        this.amperage = energyPath.amperage;
        this.loss = energyPath.loss;
        this.from = energyPath.from;
        this.to = energyPath.to;
    }

    @Override
    public long maxAmperage() {
        return amperage;
    }

    @Override
    public long maxVoltage() {
        return voltage;
    }

    @Override
    public long loss() {
        return loss;
    }

    @Override
    public void onPassing(long voltage, long amperage) {
        for (IEnergyPassThrough passThrough : passThroughList) {
            passThrough.onPassing(voltage, amperage);
        }
    }

    public EnergyPath copy() {
        return new EnergyPath(this);
    }

    public void append(IEnergyPassThrough energyPassThrough) {
        passThroughList.add(energyPassThrough);
    }
}
