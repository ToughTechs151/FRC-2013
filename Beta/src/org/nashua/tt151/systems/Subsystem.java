package org.nashua.tt151.systems;

import java.io.IOException;
import edu.wpi.first.wpilibj.templates.Dash;
import org.nashua.tt151.libraries.Controller.DualAction;

public abstract class Subsystem {
    public abstract void initialization();
    public abstract void operatorControl(DualAction driver, DualAction shooter);
    public abstract void updateDashboard(Dash dash) throws IOException;
    public abstract void test(DualAction driver, DualAction shooter);
}
