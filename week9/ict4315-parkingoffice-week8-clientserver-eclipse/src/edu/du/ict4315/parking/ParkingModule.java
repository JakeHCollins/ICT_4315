package edu.du.ict4315.parking;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import edu.du.ict4315.parking.charges.factory.ParkingChargeCalculatorFactory;
import edu.du.ict4315.parking.charges.factory.StandardCalculatorFactory;
import edu.du.ict4315.parking.service.RegisterCarCommand;
import edu.du.ict4315.parking.service.RegisterCustomerCommand;

public class ParkingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ParkingChargeCalculatorFactory.class)
                .to(StandardCalculatorFactory.class);

        bind(RealParkingOffice.class).in(Singleton.class);

        bind(TransactionManager.class).in(Singleton.class);

        Multibinder<Command> commands = Multibinder.newSetBinder(binder(), Command.class);
        commands.addBinding().to(RegisterCustomerCommand.class);
        commands.addBinding().to(RegisterCarCommand.class);
    }
}